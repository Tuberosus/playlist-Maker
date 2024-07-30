package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: Playlist): PlayListEntity {
        return PlayListEntity(
            id = 0,
            name = playlist.name,
            description = playlist.description,
            tracksId = gson.toJson(playlist.tracksId),
            imageDir = playlist.imageDir,
            trackCount = playlist.trackCount
        )
    }

    fun map(playListEntity: PlayListEntity): Playlist {
        return Playlist(
            name = playListEntity.name,
            description = playListEntity.description,
            tracksId = gson.fromJson(playListEntity.tracksId, object : TypeToken<List<Int>>() {}.type),
            imageDir = playListEntity.imageDir,
            trackCount = playListEntity.trackCount
        )
    }
}