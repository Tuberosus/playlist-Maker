package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val searchHistory: SearchHistory):
    SearchHistoryInteractor {

    override fun read(): ArrayList<Track> {
        return searchHistory.read()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistory.addTrackToHistory(track)
    }

    override fun clear() {
        searchHistory.clear()
    }
}