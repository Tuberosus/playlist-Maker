package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    fun showFavoriteTracks(): Flow<List<Track>>
    fun insertFavoriteTrack(track: List<Track>)
    fun deleteFavoriteTrack(id: Int)
    fun getTrackIdInFavorite(): Flow<List<Int>>
}