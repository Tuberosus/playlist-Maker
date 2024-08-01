package com.example.playlistmaker.data.media

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor,
) : PlaylistRepository {

    override fun insertPlaylist(playlist: Playlist) {
        appDatabase.playListDao().insertPlaylist(convertor.map(playlist))
    }

    override fun updatePlaylist(name: String, tracksId: ArrayList<Int>, trackCount: Int) {
        val tracksIdJson = convertor.map(tracksId)
        appDatabase.playListDao().updatePlaylist(name, tracksIdJson, trackCount)
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