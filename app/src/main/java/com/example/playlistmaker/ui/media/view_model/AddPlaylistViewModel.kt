package com.example.playlistmaker.ui.media.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist

open class AddPlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    open var playlistName: String? = null
    open var playlistDescription: String? = null
    open var imageUri: Uri? = null
    open var imagePath: String? = null

    suspend fun savePlaylist() {
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