package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(tracks: List<TrackEntity>)

    @Query("DELETE FROM $TABLE_NAME WHERE trackId = :id")
    fun deleteFavorite(id: Int)

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id DESC")
    suspend fun getFavoriteTrack(): List<TrackEntity>

    @Query("SELECT trackId FROM $TABLE_NAME")
    suspend fun getTrackIdInFavorite(): List<Int>

    companion object {
        const val TABLE_NAME = "track_table"
    }
}