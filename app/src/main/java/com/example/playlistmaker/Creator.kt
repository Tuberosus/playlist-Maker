package com.example.playlistmaker

import com.example.playlistmaker.data.impl.GetTrackImpl
import com.example.playlistmaker.data.impl.MediaPlayerManagerImpl
import com.example.playlistmaker.domain.GetTrackUseCaseImpl
import com.example.playlistmaker.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.api.GetTrack
import com.example.playlistmaker.domain.api.GetTrackUseCase
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerManager

object Creator {
    fun provideGetTrackUseCase(): GetTrackUseCase {
        return GetTrackUseCaseImpl(provideGetTrack())
    }

    private fun provideGetTrack(): GetTrack {
        return GetTrackImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerManager())
    }

    private fun provideMediaPlayerManager(): MediaPlayerManager {
        return MediaPlayerManagerImpl()
    }

}