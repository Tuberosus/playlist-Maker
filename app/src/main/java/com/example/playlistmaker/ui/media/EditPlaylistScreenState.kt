package com.example.playlistmaker.ui.media

sealed interface EditPlaylistScreenState {
    data class PlaylistInfo(
        val filePath: String?,
        val playlistName: String?,
        val playlistDescription: String?
    ) : EditPlaylistScreenState
}