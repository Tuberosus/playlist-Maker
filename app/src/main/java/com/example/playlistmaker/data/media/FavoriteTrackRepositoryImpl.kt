package com.example.playlistmaker.data.media

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTrackRepository {

    override fun showFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTrack()
        emit(convertFromTrackEntity(tracks))
    }

    override fun insertFavoriteTrack(track: List<Track>) {
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
    }

    override fun deleteFavoriteTrack(id: Int) {
        appDatabase.trackDao()
            .deleteFavorite(id)
    }

    override fun getTrackIdInFavorite(): Flow<List<Int>> = flow {
        val ids = appDatabase.trackDao().getTrackIdInFavorite()
        emit(ids)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity ->
            trackDbConvertor.map(trackEntity)
        }
    }

    private fun convertToTrackEntity(tracks: List<Track>): List<TrackEntity> {
        return tracks.map { track ->
            trackDbConvertor.map(track)
        }
    }
}