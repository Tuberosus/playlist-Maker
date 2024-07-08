package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchTrackInteractor
import com.example.playlistmaker.domain.search.api.SearchTrackRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository):
    SearchTrackInteractor {
    override fun searchTrack(expression: String): Flow<ArrayList<Track>?> {
        return repository.getTrackList(expression).map { result ->
            when (result) {
                is Resource.Success -> { result.data }
                is Resource.Error -> { null }
            }
        }
    }
}