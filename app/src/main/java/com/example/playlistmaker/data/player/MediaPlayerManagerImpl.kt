package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.MediaPlayerManager
import com.example.playlistmaker.domain.player.PlayerState

class MediaPlayerManagerImpl: MediaPlayerManager {

    private val mediaPlayer = MediaPlayer()
    override var state = PlayerState.DEFAULT

    override fun preparePlayer(
        url: String,
//        callback: () -> Unit
    ) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                state = PlayerState.PREPARED
            }
            setOnCompletionListener {
                state = PlayerState.DONE
            }
        }
    }

    override fun play() {
        mediaPlayer.start()
        state = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        state = PlayerState.PAUSED
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }
}