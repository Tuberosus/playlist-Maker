package com.example.playlistmaker

import com.example.playlistmaker.data.impl.GetTrackImpl
import com.example.playlistmaker.data.impl.MediaPlayerManagerImpl
import com.example.playlistmaker.domain.GetTrackUseCase
import com.example.playlistmaker.domain.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.GetTrack
import com.example.playlistmaker.domain.api.MediaPlayerManager

object Creator {
    fun provideGetTrackUseCase(): GetTrackUseCase {
        return GetTrackUseCase(provideGetTrack())
    }

    private fun provideGetTrack(): GetTrack {
        return GetTrackImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractor(provideMediaPlayerManager())
    }

    private fun provideMediaPlayerManager(): MediaPlayerManager {
        return MediaPlayerManagerImpl()
    }

}