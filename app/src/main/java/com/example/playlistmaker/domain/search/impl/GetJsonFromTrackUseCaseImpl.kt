package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.GetJsonFromTrack
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase

class GetJsonFromTrackUseCaseImpl(private val getJsonFromTrack: GetJsonFromTrack): GetJsonFromTrackUseCase {
    override fun execute(track: Track): String {
        return getJsonFromTrack.execute(track)
    }
}