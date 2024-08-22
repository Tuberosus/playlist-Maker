package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.LinkPlaylistTrackEntity
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistsEntity
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: Playlist): PlayListEntity {
        return PlayListEntity(
            id = playlist.id,
            name = playlist.name!!,
            description = playlist.description,
//            tracksId = gson.toJson(playlist.tracksId),
            imageDir = playlist.imageDir,
            trackCount = playlist.trackCount
        )
    }

    fun map(playListEntity: PlayListEntity): Playlist {
        return Playlist(
            id = playListEntity.id,
            name = playListEntity.name,
            description = playListEntity.description,
//            tracksId = gson.fromJson(
//                playListEntity.tracksId,
//                object : TypeToken<ArrayList<Int>>() {}.type
//            ),
            imageDir = playListEntity.imageDir,
            trackCount = playListEntity.trackCount
        )
    }

    fun map(track: Track): TrackInPlaylistsEntity {
        return TrackInPlaylistsEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(trackInPlaylistsEntity: TrackInPlaylistsEntity): Track {
        return Track(
            trackId = trackInPlaylistsEntity.trackId,
            trackName = trackInPlaylistsEntity.trackName,
            artistName = trackInPlaylistsEntity.artistName,
            trackTimeMillis = trackInPlaylistsEntity.trackTimeMillis,
            artworkUrl100 = trackInPlaylistsEntity.artworkUrl100,
            collectionName = trackInPlaylistsEntity.collectionName,
            releaseDate = trackInPlaylistsEntity.releaseDate,
            primaryGenreName = trackInPlaylistsEntity.primaryGenreName,
            country = trackInPlaylistsEntity.country,
            previewUrl = trackInPlaylistsEntity.previewUrl
        )
    }

    fun idsToJson(ids: ArrayList<Int>): String {
        return gson.toJson(ids)
    }

    fun toLinkPlaylistTrackEntity(playlistId: Int, trackId: Int): LinkPlaylistTrackEntity {
        return LinkPlaylistTrackEntity(
            playlistId = playlistId,
            trackId = trackId
        )
    }
}