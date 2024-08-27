package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.EditPlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistItemViewModel(
    private val interactor: PlaylistInteractor,
    private var playlist: Playlist
) : AddPlaylistViewModel(interactor) {

    private val playlistInfoLiveData = MutableLiveData<EditPlaylistScreenState>()
    fun getPlaylistInfoObserver(): LiveData<EditPlaylistScreenState> = playlistInfoLiveData

    init {
        getPlaylistAttributes()
    }

    fun getPlaylistAttributes() {
        playlistInfoLiveData.value = EditPlaylistScreenState.PlaylistInfo(playlist)
    }


    fun updatePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            if (super.imageUri != null) {
                if (playlist.imageDir != null) interactor.deleteImage(playlist.imageDir!!)
                super.imagePath = interactor.saveImageToPrivateStorage(imageUri!!, playlistName!!)
            } else {
                super.imagePath = playlist.imageDir
            }
            val newPlaylist = Playlist(
                id = playlist.id,
                name = super.playlistName,
                description = super.playlistDescription,
                imageDir = super.imagePath,
                trackCount = playlist.trackCount
            )
            interactor.fullUpdatePlaylist(newPlaylist)
            playlist = newPlaylist
        }
    }
}