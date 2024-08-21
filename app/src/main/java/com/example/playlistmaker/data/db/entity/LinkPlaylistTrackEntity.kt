package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import com.example.playlistmaker.data.db.dao.LinkPlaylistTrackDao

@Entity(
    tableName = LinkPlaylistTrackDao.TABLE_NAME,
    primaryKeys = ["playlistId", "trackId"]
)
class LinkPlaylistTrackEntity(
    val playlistId: Int,
    val trackId: Int
)