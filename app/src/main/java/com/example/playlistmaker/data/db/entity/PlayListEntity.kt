package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.data.db.dao.PlaylistDao

@Entity(tableName = PlaylistDao.TABLE_NAME)
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
//    val tracksId: String?,
    val imageDir: String?,
    val trackCount: Int
)