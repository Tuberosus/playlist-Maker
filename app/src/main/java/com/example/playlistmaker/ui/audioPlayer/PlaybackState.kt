package com.example.playlistmaker.ui.audioPlayer

sealed interface PlaybackState {
    data object Play: PlaybackState
    data object Pause: PlaybackState
}