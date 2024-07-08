package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface SearchTrackInteractor {
    fun searchTrack(expression: String): Flow<ArrayList<Track>?>
}