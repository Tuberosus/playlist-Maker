package com.example.playlistmaker.data.media

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistsEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.TrackCountStringBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor,
    private val context: Context
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playListDao().insertPlaylist(convertor.map(playlist))
    }

    override fun updatePlaylist(playlist: Playlist, track: Track): Boolean {

//        if (playlist.tracksId.contains(track.trackId)) return false
        val playlistId = appDatabase.linkPlaylistTrack().checkTrackInPlaylist(track.trackId, playlist.id)
        Log.d("MyTag", playlistId.toString())
        if (playlistId != 0) return false

        playlist.apply {
//            tracksId.add(track.trackId)
            trackCount += 1
        }
        appDatabase.playListDao().updatePlaylist(
            playlist.id,
            playlist.trackCount
        )
        appDatabase.tracksInPlaylist().addTrack(
            convertor.map(track)
        )
        appDatabase.linkPlaylistTrack().insertLink(
            convertor.toLinkPlaylistTrackEntity(
                playlistId = playlist.id,
                trackId = track.trackId
            )
        )
        return true
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playListDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistByName(playlistName: String): Flow<Playlist> = flow {
        val playlist = appDatabase.playListDao().getPlaylistByName(playlistName)
        emit(convertor.map(playlist))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist? {
        val playListEntity = appDatabase.playListDao().getPlaylistById(playlistId)
        return if (playListEntity == null) null else convertor.map(playListEntity)
    }

    override suspend fun getTrackOfPlaylist(playlistId: Int): List<Track> {
        val tracksId = appDatabase.linkPlaylistTrack().getTracksOfPlaylist(playlistId)
        val trackInPlaylistEntity = appDatabase.tracksInPlaylist().getTracksOfPlaylist(tracksId)
        return convertFromTrackInPlaylistEntity(trackInPlaylistEntity)
    }

    override suspend fun getTotalDuration(playlistId: Int): Long {
        var totalDurationMillis = 0L
        val tracks = getTrackOfPlaylist(playlistId)
        tracks.forEach { track ->
            totalDurationMillis += track.trackTimeMillis.toLong()
        }
        return totalDurationMillis
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        val playlist = getPlaylistById(playlistId)
        val trackCount = playlist!!.trackCount - 1
        appDatabase.playListDao().updatePlaylist(playlistId, trackCount)
        val linkPlaylistTrackEntity = convertor.toLinkPlaylistTrackEntity(playlistId, trackId)
        appDatabase.linkPlaylistTrack().deleteTrackFromPlaylist(linkPlaylistTrackEntity)
        if (!isTrackInPlaylists(trackId)) appDatabase.tracksInPlaylist().deleteTrackById(trackId)
    }

    override suspend fun sharePlaylist(playlistId: Int): Boolean {
        val tracks = getTrackOfPlaylist(playlistId)

        if (tracks.isEmpty()) return false

        val countTrack = TrackCountStringBuilder(context).build(tracks.size)
        var countForList = 0
        var stringListOfTrack = ""
        tracks.forEach { track ->
            countForList += 1
            stringListOfTrack += "\n${countForList}. ${track.artistName} - ${track.trackName} (${track.trackTime})"
        }
        val playlistText = "$countTrack$stringListOfTrack"
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, playlistText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)

        return true
    }

    override suspend fun deletePlaylist(playlist: Playlist, trackList: List<Track>) {
        trackList.forEach { track ->
            deleteTrackFromPlaylist(playlist.id, track.trackId)
            Log.d("MyTag", "del ${track.trackId}")
        }
        appDatabase.playListDao().deletePlaylist(
            convertor.map(playlist)
        )

        Log.d("MyTag", "del playlist")
    }

    override suspend fun fullUpdatePlaylist(playlist: Playlist) {
        val playListEntity = convertor.map(playlist)
        appDatabase.playListDao().fullUpdate(playListEntity)
    }

    private fun isTrackInPlaylists(trackId: Int): Boolean {
        val check = appDatabase.linkPlaylistTrack().isTrackInPlaylists(trackId)
        return check > 0
    }

    private fun convertFromPlaylistEntity(playlists: List<PlayListEntity>): List<Playlist> {
        return playlists.map { convertor.map(it) }
    }

    private fun convertFromTrackInPlaylistEntity(trackEntities: List<TrackInPlaylistsEntity>): List<Track> {
        return trackEntities.map { convertor.map(it) }
    }

}