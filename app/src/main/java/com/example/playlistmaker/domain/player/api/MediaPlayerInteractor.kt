package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerInteractor {
    fun getState(): PlayerState
    fun setState(state: PlayerState)
    fun getCurrentPosition(): Int
    fun release()
    fun preparePlayer(url: String,
//                      callback: ()-> Unit
                    )
    fun play(callback: ()-> Unit)
    fun pause(callback: ()-> Unit)
}