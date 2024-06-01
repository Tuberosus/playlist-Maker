package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.GetTrackUseCaseImpl
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchTrackInteractor
import com.example.playlistmaker.domain.search.impl.GetJsonFromTrackUseCaseImpl
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    // Settings and Sharing
    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    // Search
    single<SearchTrackInteractor> {
        SearchTrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<GetJsonFromTrackUseCase> {
        GetJsonFromTrackUseCaseImpl(get())
    }

    // Player
    single<GetTrackUseCase> {
        GetTrackUseCaseImpl(get())
    }

    single<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }
}