package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.SongsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SongsResponse>
}