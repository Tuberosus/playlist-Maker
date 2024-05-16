package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.GetJsonFromTrack
import com.google.gson.Gson

class GetJsonFromTrackImpl: GetJsonFromTrack {
    override fun execute(track: Track): String {
        return Gson().toJson(track)
    }

}