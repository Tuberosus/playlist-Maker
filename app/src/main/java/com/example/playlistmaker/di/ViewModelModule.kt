package com.example.playlistmaker.di

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.audioPlayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.view_model.FavoriteTrackViewModel
import com.example.playlistmaker.ui.media.view_model.AddPlaylistViewModel
import com.example.playlistmaker.ui.media.view_model.EditPlaylistItemViewModel
import com.example.playlistmaker.ui.media.view_model.PlaylistItemViewModel
import com.example.playlistmaker.ui.media.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel { (jsonTrack: String) ->
        AudioPlayerViewModel(get(), get(), get(), jsonTrack, get(), androidApplication())
    }

    viewModel {
        FavoriteTrackViewModel(get(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        AddPlaylistViewModel(get())
    }

    viewModel { (playlistId: Int) ->
        PlaylistItemViewModel(playlistId, get(), get(), androidApplication())
    }

    viewModel { (playlist: Playlist) ->
        EditPlaylistItemViewModel(get(), playlist)
    }
}