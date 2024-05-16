package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface GetJsonFromTrackUseCase {
    fun execute(track: Track): String
}