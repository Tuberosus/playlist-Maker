package com.example.playlistmaker.data.search.dto

class SongsResponse(val resultCount: Int,
                    val results: ArrayList<TrackDTO>): Response()