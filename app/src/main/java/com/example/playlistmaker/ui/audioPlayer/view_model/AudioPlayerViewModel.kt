package com.example.playlistmaker.ui.audioPlayer.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.AudioPlayerScreenState
import com.example.playlistmaker.ui.audioPlayer.BottomSheetScreenState
import com.example.playlistmaker.ui.audioPlayer.ToastState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackUseCase,
    private val playerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val jsonTrack: String,
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application
) : ViewModel() {

    companion object {
        private const val TIMER_DELAY = 300L
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var timerJob: Job? = null

    private var isFavorite = false
    val track: Track = getTrackUseCase.execute(jsonTrack)

    private var currentPlayerState = PlayerState.DEFAULT

    private val audioPlayerStateLiveData = MutableLiveData<AudioPlayerScreenState>()
    private val playlistLiveData = MutableLiveData<BottomSheetScreenState>()
    private val toastTextLiveData = MutableLiveData<ToastState>()

    fun getAudioPlayerState(): LiveData<AudioPlayerScreenState> = audioPlayerStateLiveData
    fun getBottomSheetState(): LiveData<BottomSheetScreenState> = playlistLiveData
    fun getToastTextLiveData(): LiveData<ToastState> = toastTextLiveData


    init {
        viewModelScope.launch {
            loadPlayer()
            isInFavorite()
            getPlaylistState()
        }
    }


    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        stopTimerUpdate()
    }

    fun onPause() = pause()

    private fun play() {
        playerInteractor.play()
        startTimerUpdate()
        playerInteractor.onCompletionWork { playbackControl() }
    }

    private fun pause() {
        playerInteractor.pause()
        audioPlayerStateLiveData.postValue(AudioPlayerScreenState.Pause)
        stopTimerUpdate()
    }

    fun playbackControl() {
        currentPlayerState = playerInteractor.getState()
        when (currentPlayerState) {
            PlayerState.PLAYING -> pause()
            PlayerState.PAUSED -> play()
            PlayerState.PREPARED -> play()
            PlayerState.DONE -> {
                audioPlayerStateLiveData.postValue(AudioPlayerScreenState.Default)
                stopTimerUpdate()
                playerInteractor.setState(PlayerState.PREPARED)
            }

            else -> null
        }
    }

    fun getCurrentPosition(): String {
        return dateFormat.format(
            playerInteractor.getCurrentPosition()
        )
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                val time = dateFormat.format(
                    playerInteractor.getCurrentPosition()
                )
                audioPlayerStateLiveData.postValue(AudioPlayerScreenState.Play(time))
                delay(TIMER_DELAY)
            }
        }
    }

    private fun stopTimerUpdate() {
        timerJob = null
    }

    fun clickOnLike() {
        isFavorite = !isFavorite
        audioPlayerStateLiveData.postValue(AudioPlayerScreenState.TrackLike(isFavorite))

        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                favoriteTrackInteractor.insertFavoriteTrack(track)
            } else {
                favoriteTrackInteractor.deleteFavoriteTrack(track.trackId)
            }
        }
    }

    private fun loadPlayer() {
        audioPlayerStateLiveData.value = AudioPlayerScreenState.LoadTrack(track)
        viewModelScope.launch {
            playerInteractor.preparePlayer(track.previewUrl!!)

        }
    }

    private fun isInFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val trackId = getTrackUseCase.execute(jsonTrack).trackId
            favoriteTrackInteractor.getTrackIdInFavorite()
                .collect { ids ->
                    isFavorite = ids.contains(trackId)
                    audioPlayerStateLiveData.postValue(AudioPlayerScreenState.TrackLike(isFavorite))
                }
        }
    }

    fun getPlaylistState() {
        Log.d("MyTag", "get playlist")
        playlistLiveData.value = BottomSheetScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists()
                .collect { playlist ->
                    playlistLiveData.postValue(
                        BottomSheetScreenState.Content(playlist)
                    )
                }
        }
    }

    fun putTrackIntoPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val isAdd = playlistInteractor.updatePlaylist(playlist, track)
            withContext(Dispatchers.Main) {
                makeToastText(isAdd, playlist.name!!)
            }
        }
        getPlaylistState()
    }

    private fun makeToastText(isAdd: Boolean, playlistName: String) {
        toastTextLiveData.value =
            if (isAdd) {
                ToastState.ShowSuccess(
                    application.getString(R.string.addPlaylistSuccess, playlistName)
                )
            } else {
                ToastState.ShowError(
                    application.getString(R.string.addPlaylistError, playlistName)
                )
            }
    }

    fun toastWasShown() {
        toastTextLiveData.value = ToastState.None
    }

}