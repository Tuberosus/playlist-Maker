package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.db.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun showFavoriteTracks(): Flow<List<Track>> {
        return favoriteTrackRepository.showFavoriteTracks()
    }

    override fun deleteFavoriteTrack(id: Int) {
        favoriteTrackRepository.deleteFavoriteTrack(id)
    }

    override fun insertFavoriteTrack(track: Track) {
        val tracks = listOf(track)
        favoriteTrackRepository.insertFavoriteTrack(tracks)
    }

    override fun getTrackIdInFavorite(): Flow<List<Int>> {
        return favoriteTrackRepository.getTrackIdInFavorite()
    }
}