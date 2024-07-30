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

    override fun updatePlaylist(playlist: Playlist) {
        appDatabase.playListDao().updatePlaylist(convertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playListDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlayListEntity>): List<Playlist> {
        return playlists.map { convertor.map(it) }
    }

}