package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.LinkPlaylistTrackDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.dao.TracksInPlaylistDao
import com.example.playlistmaker.data.db.entity.LinkPlaylistTrackEntity
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistsEntity

@Database(version = 4, entities = [
    TrackEntity::class,
    PlayListEntity::class,
    TrackInPlaylistsEntity::class,
    LinkPlaylistTrackEntity::class
])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlaylistDao
    abstract fun tracksInPlaylist(): TracksInPlaylistDao
    abstract fun linkPlaylistTrack(): LinkPlaylistTrackDao
}