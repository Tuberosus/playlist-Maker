package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun insertPlaylist(playlist: Playlist)
    fun updatePlaylist(name: String, tracksId: ArrayList<Int>, trackCount: Int)
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistByName(playlistName: String): Flow<Playlist>
}