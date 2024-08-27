package com.example.playlistmaker.domain.media.impl

import android.net.Uri
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.api.ExternalFilesNavigator
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Executors

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository,
    private val externalFilesNavigator: ExternalFilesNavigator
) : PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override fun updatePlaylist(playlist: Playlist, track: Track): Boolean {
        return repository.updatePlaylist(playlist, track)
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

    override fun deleteImage(imageDir: String) {
        externalFilesNavigator.deleteImage(imageDir)
    }

    override fun getPlaylistByName(playlistName: String): Flow<Playlist> {
        return repository.getPlaylistByName(playlistName)
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist? {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun getTrackOfPlaylist(playlistId: Int): List<Track> {
        return repository.getTrackOfPlaylist(playlistId)
    }

    override suspend fun getTotalDuration(playlistId: Int): Long {
        return repository.getTotalDuration(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        repository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun sharePlaylist(playlistId: Int): Boolean {
        return repository.sharePlaylist(playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist, trackList: List<Track>) {
        repository.deletePlaylist(playlist, trackList)
    }

    override suspend fun fullUpdatePlaylist(playlist: Playlist) {
        repository.fullUpdatePlaylist(playlist)
    }

}