package com.example.playlistmaker.data.player

import com.example.playlistmaker.domain.player.api.GetTrack
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class GetTrackImpl : GetTrack {
    override fun execute(jsonString: String):Track {
        return Gson().fromJson(jsonString, Track::class.java)
    }

}