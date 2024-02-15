package com.example.playlistmaker.services.itunes

import com.example.playlistmaker.Track

class SongsResponse(val resultCount: Int,
                    val results: List<Track>)