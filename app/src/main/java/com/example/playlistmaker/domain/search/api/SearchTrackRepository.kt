package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    fun getTrackList(expression: String): Flow<Resource<ArrayList<Track>>>
}