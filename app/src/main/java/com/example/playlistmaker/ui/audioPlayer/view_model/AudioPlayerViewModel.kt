package com.example.playlistmaker.ui.audioPlayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Utils.Creator
import com.example.playlistmaker.Utils.SingleEventLiveData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.PlaybackState
import com.example.playlistmaker.ui.audioPlayer.PlayerScreenState

class AudioPlayerViewModel(
    private val jsonTrack: String,
    private val getTrackUseCase: GetTrackUseCase,
    private val playerInteractor: MediaPlayerInteractor,
): ViewModel() {

//    init {
//        loadPlayer()
//    }
    companion object {
        fun getViewModelFactory(jsonTrack: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val getTrackUseCase = Creator.provideGetTrackUseCase()
                val playerInteractor = Creator.provideMediaPlayerInteractor()

                AudioPlayerViewModel(
                    jsonTrack,
                    getTrackUseCase,
                    playerInteractor
                )
            }
        }
    }

    private val screenStateLiveData = SingleEventLiveData<Track>()
    private val playbackStateLiveData = MutableLiveData<PlaybackState>()
    fun getScreenStateLiveData(): LiveData<Track> = screenStateLiveData
    fun getPlaybackStateLiveData(): LiveData<PlaybackState> = playbackStateLiveData

    fun loadPlayer() {
        val track = getTrackUseCase.execute(jsonTrack)
        screenStateLiveData.value = track


    }
}