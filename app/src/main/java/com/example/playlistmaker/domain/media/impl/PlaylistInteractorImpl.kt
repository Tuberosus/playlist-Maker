package com.example.playlistmaker.domain.media.impl

import android.net.Uri
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.api.ExternalFilesNavigator
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository,
    private val externalFilesNavigator: ExternalFilesNavigator
) : PlaylistInteractor {

    override fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun saveImageToPrivateStorage(uri: Uri, fileName: String): String {
        return externalFilesNavigator.saveImageToPrivateStorage(uri, fileName)
    }

    override fun getImageFromPrivateStorage(name: String): Uri {
        return externalFilesNavigator.getImageFromPrivateStorage(name)
    }

}