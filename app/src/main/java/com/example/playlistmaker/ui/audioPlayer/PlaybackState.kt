package com.example.playlistmaker.ui.audioPlayer

sealed interface PlaybackState {
    data class Play(val time: String) : PlaybackState
    data object Pause: PlaybackState
    data object Default: PlaybackState
}