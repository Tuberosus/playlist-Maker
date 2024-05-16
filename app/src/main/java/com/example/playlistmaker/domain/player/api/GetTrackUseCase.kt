package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.models.Track

interface GetTrackUseCase {
    fun execute(jsonString: String): Track
}