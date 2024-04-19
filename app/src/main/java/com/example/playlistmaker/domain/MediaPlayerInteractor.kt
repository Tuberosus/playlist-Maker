package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.MediaPlayerManager
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerInteractor(private val mediaPlayerManager: MediaPlayerManager) {

    fun getState(): PlayerState {
        return mediaPlayerManager.state
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }

    fun release() {
        mediaPlayerManager.release()
    }

    fun preparePlayer(url: String, callback: () -> Unit) {
        mediaPlayerManager.preparePlayer(url, callback)
    }
    fun play(callback: () -> Unit) {
        mediaPlayerManager.play()
        callback()
    }

    fun pause(callback: () -> Unit) {
        mediaPlayerManager.pause()
        callback()
    }
}