package com.example.playlistmaker.data.media

import android.util.Log
import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor,
) : PlaylistRepository {

    override fun insertPlaylist(playlist: Playlist) {
        Log.d("MyTag", "add playlist")
        appDatabase.playListDao().insertPlaylist(convertor.map(playlist))
    }

    override fun updatePlaylist(playlist: Playlist, track: Track): Boolean {

        if (playlist.tracksId.contains(track.trackId)) return false

        playlist.apply {
            tracksId.add(track.trackId)
            trackCount += 1
        }
        appDatabase.playListDao().updatePlaylist(
            playlist.name,
            convertor.idsToJson(playlist.tracksId),
            playlist.trackCount
        )
        appDatabase.tracksInPlaylist().addTrack(
            convertor.map(track)
        )
        return true
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playListDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistByName(playlistName: String): Flow<Playlist> = flow {
        val playlist = appDatabase.playListDao().getPlaylistByName(playlistName)
        emit(convertor.map(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlayListEntity>): List<Playlist> {
        return playlists.map { convertor.map(it) }
    }

}