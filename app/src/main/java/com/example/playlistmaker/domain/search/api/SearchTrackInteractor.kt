package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.models.Track

interface SearchTrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTrack: ArrayList<Track>?)
    }
}