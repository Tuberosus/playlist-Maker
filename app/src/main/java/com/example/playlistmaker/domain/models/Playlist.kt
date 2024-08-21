package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String?,
//    val tracksId: ArrayList<Int> = ArrayList(),
    val imageDir: String?,
    var trackCount: Int = 0
)