package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.player.PlayerState

interface PlayerListener {
    fun onStateChange(state: PlayerState)
}