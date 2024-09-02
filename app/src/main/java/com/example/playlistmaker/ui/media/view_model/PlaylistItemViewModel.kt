package com.example.playlistmaker.ui.media.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase
import com.example.playlistmaker.ui.media.PlaylistItemScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistItemViewModel(
    private val playlistId: Int,
    private val playlistInteractor: PlaylistInteractor,
    private val getJsonFromTrackUseCase: GetJsonFromTrackUseCase,
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("mm", Locale.getDefault())

    private val screenStateLiveData = MutableLiveData<PlaylistItemScreenState>()
    private val trackClickEvent = MutableLiveData<String>()
    fun screenStateObserver(): LiveData<PlaylistItemScreenState> = screenStateLiveData
    fun getTrackClickEvent(): LiveData<String> = trackClickEvent

    fun getScreenState(playlistId: Int) {
        screenStateLiveData.postValue(PlaylistItemScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = getPlaylistById(playlistId)
            if (playlist != null) {
                val trackList = getTrackList(playlistId)
                val duration = getTotalDurationInMinutes(playlistId, trackList)
                Log.d("MyTag", playlist.imageDir.toString())
                screenStateLiveData.postValue(
                    PlaylistItemScreenState.Content(playlist, duration, trackList)
                )
            }
        }
        Log.d("MyTag", "getScreenState Done")
    }

    fun onTrackClick(track: Track): String {
        return getJsonFromTrackUseCase.execute(track)
    }

    fun deleteTrackFromPlaylist(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                playlistInteractor.deleteTrackFromPlaylist(playlistId, trackId)
            }.join()
            getScreenState(playlistId)
        }
    }

    fun sharePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val isShare = playlistInteractor.sharePlaylist(playlistId)
            if (!isShare) screenStateLiveData.postValue(
                PlaylistItemScreenState.EmptyShare(
                        R.string.empty_share_toast
                )
            )
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val trackList = getTrackList(playlistId)
            playlistInteractor.deletePlaylist(playlist, trackList)
        }
    }

    private suspend fun getPlaylistById(playlistId: Int): Playlist? {
        return playlistInteractor.getPlaylistById(playlistId)
    }

    private suspend fun getTotalDurationInMinutes(playlistId: Int, trackList: List<Track>): String {
        var duration = 0L
        if (trackList.isNotEmpty()) duration = playlistInteractor.getTotalDuration(playlistId)
        return dateFormat.format(duration)
    }

    private suspend fun getTrackList(playlistId: Int): List<Track> {
        return playlistInteractor.getTrackOfPlaylist(playlistId)
    }
}