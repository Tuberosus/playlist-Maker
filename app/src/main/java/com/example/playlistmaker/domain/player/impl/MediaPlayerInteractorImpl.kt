package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerManager
import com.example.playlistmaker.domain.player.PlayerState

class MediaPlayerInteractorImpl(
    private val mediaPlayerManager: MediaPlayerManager
) : MediaPlayerInteractor {

    override fun getState(): PlayerState {
        return mediaPlayerManager.state
    }

    override fun setState(state: PlayerState) {
        mediaPlayerManager.state = state
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerManager.getCurrentPosition()
    }

    override fun release() {
        mediaPlayerManager.release()
    }

    override fun preparePlayer(url: String) {
        mediaPlayerManager.preparePlayer(url)
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