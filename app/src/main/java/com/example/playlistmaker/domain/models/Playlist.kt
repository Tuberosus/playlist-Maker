package com.example.playlistmaker.domain.models

data class Playlist(
    val name: String,
    val description: String?,
    val tracksId: List<Int>?,
    val imageDir: String?,
    val trackCount: Int = 0
)