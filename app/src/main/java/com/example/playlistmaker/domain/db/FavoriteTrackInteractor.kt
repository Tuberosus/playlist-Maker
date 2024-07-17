package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    fun showFavoriteTracks(): Flow<List<Track>>
    fun insertFavoriteTrack(track: Track)
    fun deleteFavoriteTrack(id: Int)
    fun getTrackIdInFavorite(): Flow<List<Int>>
}