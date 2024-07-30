package com.example.playlistmaker.ui.audioPlayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.PlaybackState
import com.example.playlistmaker.ui.audioPlayer.BottomSheetScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackUseCase,
    private val playerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val jsonTrack: String,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val TIMER_DELAY = 300L
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var timerJob: Job? = null

    var isFavorite = false
    private val track = getTrackUseCase.execute(jsonTrack)

    private var currentPlayerState = PlayerState.DEFAULT

    private val screenStateLiveData = MutableLiveData<Track>()
    private val playbackStateLiveData = MutableLiveData<PlaybackState>()
    private val isLikeLiveData = MutableLiveData<Boolean>()
    private val playlistLiveData = MutableLiveData<BottomSheetScreenState>()

    fun getScreenStateLiveData(): LiveData<Track> = screenStateLiveData
    fun getPlaybackStateLiveData(): LiveData<PlaybackState> = playbackStateLiveData
    fun getIsLikeLiveData(): LiveData<Boolean> = isLikeLiveData
    fun getBottomSheetState(): LiveData<BottomSheetScreenState> = playlistLiveData


    init {
        isInFavorite()
        getPlaylistState()
        loadPlayer()
    }


    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        stopTimerUpdate()
    }

    fun onPause() = pause()

    fun loadPlayer() {
        screenStateLiveData.value = track
        viewModelScope.launch {
            playerInteractor.preparePlayer(track.previewUrl!!)
        }
    }

    private fun play() {
        playerInteractor.play()
        startTimerUpdate()
        playerInteractor.onCompletionWork { playbackControl() }
    }

    private fun pause() {
        playerInteractor.pause()
        playbackStateLiveData.postValue(PlaybackState.Pause)
        stopTimerUpdate()
    }

    fun playbackControl() {
        currentPlayerState = playerInteractor.getState()
        when (currentPlayerState) {
            PlayerState.PLAYING -> pause()
            PlayerState.PAUSED -> play()
            PlayerState.PREPARED -> play()
            PlayerState.DONE -> {
                playbackStateLiveData.postValue(PlaybackState.Default)
                stopTimerUpdate()
                playerInteractor.setState(PlayerState.PREPARED)
            }

            else -> null
        }
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                val time = dateFormat.format(
                    playerInteractor.getCurrentPosition()
                )
                playbackStateLiveData.postValue(PlaybackState.Play(time))
                delay(TIMER_DELAY)
            }
        }
    }

    private fun stopTimerUpdate() {
        timerJob = null
    }

    fun clickOnLike() {
        isFavorite = !isFavorite
        isLikeLiveData.postValue(isFavorite)

        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite) {
                favoriteTrackInteractor.insertFavoriteTrack(track)
            } else {
                favoriteTrackInteractor.deleteFavoriteTrack(track.trackId)
            }
        }
    }

    private fun isInFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val trackId = getTrackUseCase.execute(jsonTrack).trackId
            favoriteTrackInteractor.getTrackIdInFavorite()
                .collect { ids ->
                    isFavorite = ids.contains(trackId)
                    isLikeLiveData.postValue(isFavorite)
                }
        }
    }

    fun getPlaylistState() {
        playlistLiveData.postValue(BottomSheetScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists()
                .collect { playlist ->
                    playlistLiveData.postValue(
                        BottomSheetScreenState.Content(playlist)
                    )
                }
        }
    }

}