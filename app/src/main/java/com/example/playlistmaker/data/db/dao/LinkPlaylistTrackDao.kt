package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.LinkPlaylistTrackEntity

@Dao
interface LinkPlaylistTrackDao {

    companion object {
        const val TABLE_NAME = "link_playlist_track_table"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLink(link: LinkPlaylistTrackEntity)

    @Delete
    fun deleteTrackFromPlaylist(linkPlaylistTrackEntity: LinkPlaylistTrackEntity)

    @Query("SELECT trackId FROM $TABLE_NAME WHERE playlistId = :playlistId")
    fun getTracksOfPlaylist(playlistId: Int): List<Int>

    @Query("SELECT playlistId FROM $TABLE_NAME WHERE trackId = :trackId AND playlistId = :playlistId")
    fun checkTrackInPlaylist(trackId: Int, playlistId: Int): Int

    @Query("SELECT COUNT(*) FROM $TABLE_NAME WHERE trackId = :trackId GROUP BY playlistId")
    fun isTrackInPlaylists(trackId: Int): Int
}