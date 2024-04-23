package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class SongsResponse(val resultCount: Int,
                    val results: ArrayList<Track>)