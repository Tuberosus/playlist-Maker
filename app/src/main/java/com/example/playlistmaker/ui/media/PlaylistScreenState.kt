package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistScreenState {
    data object Empty: PlaylistScreenState
    data object Loading: PlaylistScreenState
    data class Content(val playlists: List<Playlist>): PlaylistScreenState
    data class PlaylistClick(val playlist: Playlist): PlaylistScreenState
}