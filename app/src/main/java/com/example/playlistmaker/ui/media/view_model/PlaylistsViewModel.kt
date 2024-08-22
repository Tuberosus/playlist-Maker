package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.PlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<PlaylistScreenState>()
    val playlistObserver: LiveData<PlaylistScreenState> = playlistsLiveData

    fun getPlaylists() {
        renderState(PlaylistScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            delay(200)
            playlistInteractor.getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistScreenState.Empty)
        } else {
            renderState(PlaylistScreenState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistScreenState) {
        playlistsLiveData.postValue(state)
    }
}