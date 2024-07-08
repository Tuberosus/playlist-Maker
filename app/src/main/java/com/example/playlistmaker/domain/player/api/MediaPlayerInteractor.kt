package com.example.playlistmaker.domain.player.api

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerState

interface MediaPlayerInteractor {
    fun getState(): PlayerState
    fun setState(state: PlayerState)
    fun getCurrentPosition(): Int
    fun release()
    fun preparePlayer(url: String)
    fun play()
    fun pause()
    fun isPlaying(): Boolean
    fun onCompletionWork(callback :()->Unit)
}