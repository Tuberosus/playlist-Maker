package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TracksInPlaylistDao {

    companion object {
        const val TABLE_NAME = "tracks_in_playlist"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrack(tracksInPlaylistEntity: TrackInPlaylistsEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE trackId = :trackId")
    fun deleteTrackById(trackId: Int)

    @Query("SELECT SUM(trackId) FROM $TABLE_NAME WHERE trackId = :id GROUP BY trackId")
    fun isTrackInPlaylist(id: Int): Int

    @Query("SELECT * FROM $TABLE_NAME WHERE trackId IN (:tracksId)")
    suspend fun getTracksOfPlaylist(tracksId: List<Int>): List<TrackInPlaylistsEntity>
}