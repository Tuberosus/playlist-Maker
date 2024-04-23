package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface GetTrackUseCase {
    fun execute(jsonString: String): Track
}