package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.GetTrack
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.models.Track

class GetTrackUseCaseImpl(private val getTrack: GetTrack) : GetTrackUseCase {
    override fun execute(jsonString: String): Track {
        return getTrack.execute(jsonString)
    }
}