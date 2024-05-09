package com.example.playlistmaker.ui.audioPlayer.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.PlaybackState
import com.example.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackUseCase,
    private val playerInteractor: MediaPlayerInteractor,
): ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val getTrackUseCase = Creator.provideGetTrackUseCase()
                val playerInteractor = Creator.provideMediaPlayerInteractor()

                AudioPlayerViewModel(
                    getTrackUseCase,
                    playerInteractor
                )
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private var currentPlayerState = PlayerState.DEFAULT

    private val screenStateLiveData = MutableLiveData<Track>()
    private val playbackStateLiveData = MutableLiveData<PlaybackState>()

    fun getScreenStateLiveData(): LiveData<Track> = screenStateLiveData
    fun getPlaybackStateLiveData(): LiveData<PlaybackState> = playbackStateLiveData

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        stopTimerUpdate()
        handler.removeCallbacks(runnablePlayerState)
    }

    fun onPause() = pause()

    fun loadPlayer(jsonTrack: String) {
        val track = getTrackUseCase.execute(jsonTrack)
        screenStateLiveData.value = track

        val playerRun = Runnable {
            playerInteractor.preparePlayer(track.previewUrl!!)
        }
        handler.post(playerRun)
    }

   private fun play() {
       playerInteractor.play {
           playbackStateLiveData.postValue(PlaybackState.Play)
       }
       startTimerUpdate()
       getAutoCurrentState()
   }

    private fun pause() {
        playerInteractor.pause {
            playbackStateLiveData.postValue(PlaybackState.Pause)
        }
    }

    private val runnablePlayerState = object : Runnable {
        override fun run() {
            currentPlayerState = playerInteractor.getState()
            if (currentPlayerState == PlayerState.DONE) {
                Log.d("MyTag", currentPlayerState.toString())
                playbackControl()
            }
            handler.postDelayed(this, 500L)
        }
    }

    private fun getAutoCurrentState() {
        handler.postDelayed(runnablePlayerState, 500L)
    }
    fun playbackControl() {
        currentPlayerState = playerInteractor.getState()
        when (currentPlayerState) {
            PlayerState.PLAYING -> pause()
            PlayerState.PAUSED -> play()
            PlayerState.PREPARED -> play()
            PlayerState.DONE -> {
                playbackStateLiveData.postValue(PlaybackState.Default)
                handler.removeCallbacks(timer)
                handler.removeCallbacks(runnablePlayerState)
                playerInteractor.setState(PlayerState.PREPARED)

            }
            else -> null
        }
    }

    private val timer = object : Runnable {
        override fun run() {
            val time = SimpleDateFormat("mm:ss",
                Locale.getDefault()).format(
                playerInteractor.getCurrentPosition())
            playbackStateLiveData.postValue(PlaybackState.Timer(time))
            handler.postDelayed(this, 200)
        }
    }

    private fun startTimerUpdate() {
        handler.postDelayed(timer, 200)
    }
    private fun stopTimerUpdate() {
        handler.removeCallbacks(timer)
    }

}