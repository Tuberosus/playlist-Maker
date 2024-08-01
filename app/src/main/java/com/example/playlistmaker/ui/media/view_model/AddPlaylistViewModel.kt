package com.example.playlistmaker.ui.media.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    var playlistName: String? = null
    var playlistDescription: String? = null
    var imageUri: Uri? = null
    var imagePath: String? = null

    fun savePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            if (imageUri != null) {
                imagePath = interactor.saveImageToPrivateStorage(imageUri!!, playlistName!!)
            }
            val playlist = Playlist(
                name = playlistName!!,
                description = playlistDescription,
                imageDir = imagePath
            )
            interactor.insertPlaylist(playlist)
        }
    }
}