package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.GetTrack
import com.example.playlistmaker.domain.models.Track

class GetTrackUseCase(private val getTrack: GetTrack) {
    fun execute(jsonString: String): Track {
        return getTrack.execute(jsonString)
    }
}