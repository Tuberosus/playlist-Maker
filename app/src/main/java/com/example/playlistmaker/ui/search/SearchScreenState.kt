package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface SearchScreenState {
    data object Loading: SearchScreenState
    data class Content(val trackList: List<Track>): SearchScreenState
    data object Error : SearchScreenState
    data object Empty : SearchScreenState
    data class SearchHistoryContent(val trackList: List<Track>): SearchScreenState
}