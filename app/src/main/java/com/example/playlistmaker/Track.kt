package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

class Track (
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String // Страна исполнителя
) {
    val trackTime get() = SimpleDateFormat("mm:ss", Locale.getDefault())
        .format(trackTimeMillis.toInt())
    val artworkUrl512 get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId
    }
}