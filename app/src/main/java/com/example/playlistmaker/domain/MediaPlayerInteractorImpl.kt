package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerManager
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerInteractorImpl(
    private val mediaPlayerManager: MediaPlayerManager
) : MediaPlayerInteractor {

    override fun getState(): PlayerState {
        return mediaPlayerManager.state
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }

    override fun release() {
        mediaPlayerManager.release()
    }

    override fun preparePlayer(url: String, callback: () -> Unit) {
        mediaPlayerManager.preparePlayer(url, callback)
    }
    override fun play(callback: () -> Unit) {
        mediaPlayerManager.play()
        callback()
    }

    override fun pause(callback: () -> Unit) {
        mediaPlayerManager.pause()
        callback()
    }
}