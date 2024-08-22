package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.Playlist

sealed interface EditPlaylistScreenState {
    data class PlaylistInfo(val playlist: Playlist) : EditPlaylistScreenState
}