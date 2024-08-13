package com.example.playlistmaker.ui.audioPlayer

import com.example.playlistmaker.domain.models.Playlist

sealed interface BottomSheetScreenState {
    data object Loading : BottomSheetScreenState
    data class Content(val playlists: List<Playlist>): BottomSheetScreenState
}