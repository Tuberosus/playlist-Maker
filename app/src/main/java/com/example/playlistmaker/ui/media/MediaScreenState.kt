package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.Track

sealed interface MediaScreenState {
    data object Loading: MediaScreenState
    data class Content(val tracks: List<Track>) : MediaScreenState
    data object Empty: MediaScreenState
}