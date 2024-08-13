package com.example.playlistmaker.ui.audioPlayer

import com.example.playlistmaker.domain.models.Track

sealed interface AudioPlayerScreenState {
    data class LoadTrack(val track: Track): AudioPlayerScreenState
    data class Play(val time: String) : AudioPlayerScreenState
    data object Pause: AudioPlayerScreenState
    data object Default: AudioPlayerScreenState
    data class TrackLike(val isLike: Boolean): AudioPlayerScreenState
}