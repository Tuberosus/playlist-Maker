package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.models.Track

interface SearchTrackRepository {
    fun getTrackList(expression: String): Resource<ArrayList<Track>>
}