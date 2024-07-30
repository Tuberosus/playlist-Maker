package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.dao.PlaylistDao.Companion.TABLE_NAME
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlayListEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlayListEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getPlaylists(): List<PlayListEntity>

    companion object {
        const val TABLE_NAME = "playlist_table"
    }
}