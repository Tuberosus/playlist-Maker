package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.dao.PlaylistDao.Companion.TABLE_NAME
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlayListEntity)

    @Delete
    fun deletePlaylist(playListEntity: PlayListEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :playlistName")
    fun getPlaylistByName(playlistName: String): PlayListEntity

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getPlaylists(): List<PlayListEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlayListEntity?

    @Query("UPDATE $TABLE_NAME set " +
            "trackCount = :trackCount WHERE id = :id")
    fun updatePlaylist(id: Int, trackCount: Int)

    companion object {
        const val TABLE_NAME = "playlist_table"
    }
}