package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistory {
    fun read(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clear()
}