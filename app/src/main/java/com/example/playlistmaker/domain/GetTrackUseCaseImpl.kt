package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.GetTrack
import com.example.playlistmaker.domain.api.GetTrackUseCase
import com.example.playlistmaker.domain.models.Track

class GetTrackUseCaseImpl(private val getTrack: GetTrack) : GetTrackUseCase {
    override fun execute(jsonString: String): Track {
        return getTrack.execute(jsonString)
    }
}