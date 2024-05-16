package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun read(): List<Track>
    fun addTrackToHistory(track: Track)
    fun clear()
}