package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    fun updatePlaylist(playlist: Playlist, track: Track): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistByName(playlistName: String): Flow<Playlist>
    suspend fun getPlaylistById(playlistId: Int): Playlist?
    suspend fun getTrackOfPlaylist(playlistId: Int): List<Track>
    suspend fun getTotalDuration(playlistId: Int): Long
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)
    suspend fun sharePlaylist(playlistId: Int): Boolean
    suspend fun deletePlaylist(playlist: Playlist, trackList: List<Track>)
}