package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistItemScreenState {
    data object Loading: PlaylistItemScreenState
    data class Content(val playlist: Playlist,
                       val duration: String,
                       val trackList: List<Track>): PlaylistItemScreenState
    data class EmptyShare(val toastText: String): PlaylistItemScreenState
}