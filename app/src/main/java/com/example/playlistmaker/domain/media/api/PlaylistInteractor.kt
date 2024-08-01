package com.example.playlistmaker.domain.media.api

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun insertPlaylist(playlist: Playlist)
    fun updatePlaylist(name: String, tracksId: ArrayList<Int>, trackCount: Int)
    fun getPlaylists(): Flow<List<Playlist>>
    fun saveImageToPrivateStorage(uri: Uri, fileName: String): String
    fun getImageFromPrivateStorage(name: String): Uri
    fun getPlaylistByName(playlistName: String): Flow<Playlist>
}