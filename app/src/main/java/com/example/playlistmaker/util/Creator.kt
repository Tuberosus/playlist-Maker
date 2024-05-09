package com.example.playlistmaker.util

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.GetSharedPreferences
import com.example.playlistmaker.data.player.GetTrackImpl
import com.example.playlistmaker.data.player.MediaPlayerManagerImpl
import com.example.playlistmaker.data.search.impl.GetJsonFromTrackImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.data.search.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.player.impl.GetTrackUseCaseImpl
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.player.api.GetTrack
import com.example.playlistmaker.domain.player.api.GetTrackUseCase
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerManager
import com.example.playlistmaker.domain.search.api.GetJsonFromTrack
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchTrackInteractor
import com.example.playlistmaker.domain.search.api.SearchTrackRepository
import com.example.playlistmaker.domain.search.impl.GetJsonFromTrackUseCaseImpl
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    var application: Application? = null

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

    // Settings
    fun providerSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            provideSettingRepository(context)
        )
    }

    private fun provideSettingRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(GetSharedPreferences().execute(application!!))
    }

    // Sharing
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            provideExternalNavigator(),
            provideSharingRepository(context)
            )
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application!!)
    }

    private fun provideSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    // search
    private fun getTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitNetworkClient(application!!))
    }

    fun provideSearchTrackInteractor(): SearchTrackInteractor {
        return SearchTrackInteractorImpl(getTrackRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistory())
    }

    fun provideGetJsonFromTrackUseCase(): GetJsonFromTrackUseCase {
        return GetJsonFromTrackUseCaseImpl(provideGetJsonFromTrack())
    }

    private fun getSearchHistory(): SearchHistory {
        return SearchHistoryImpl(application!!)
    }

    private fun provideGetJsonFromTrack(): GetJsonFromTrack {
        return GetJsonFromTrackImpl()
    }
}