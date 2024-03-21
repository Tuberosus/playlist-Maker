package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
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
    val trackTime: String get() = SimpleDateFormat("mm:ss", Locale.getDefault())
        .format(trackTimeMillis.toInt())
    val artworkUrl512 get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}