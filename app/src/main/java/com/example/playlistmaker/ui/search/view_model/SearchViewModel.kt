package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.SingleEventLiveData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchTrackInteractor
import com.example.playlistmaker.ui.search.SearchScreenState

class SearchViewModel(
    private val searchTrackInteractor: SearchTrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val getJsonFromTrackUseCase: GetJsonFromTrackUseCase,
    ): ViewModel() {

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val searchTrackInteractor = Creator.provideSearchTrackInteractor()
                val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
                val getJsonFromTrackUseCase = Creator.provideGetJsonFromTrackUseCase()

                SearchViewModel(
                    searchTrackInteractor = searchTrackInteractor,
                    searchHistoryInteractor = searchHistoryInteractor,
                    getJsonFromTrackUseCase = getJsonFromTrackUseCase
                )
            }
        }

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val trackClickEvent = SingleEventLiveData<String>()
    private val state = MutableLiveData<SearchScreenState>()
    fun getState(): LiveData<SearchScreenState> = state
    fun getTrackClickEvent(): LiveData<String> = trackClickEvent

    fun loadData(expression: String) {
        if (expression.isNotEmpty()) {

            state.value = SearchScreenState.Loading

            searchTrackInteractor.searchTrack(
                expression = expression,
                object : SearchTrackInteractor.TrackConsumer{
                    override fun consume(foundTrack: ArrayList<Track>?) {
                        when {
                            foundTrack == null -> {
                                state.postValue(
                                    SearchScreenState.Error
                                )
                            }
                            foundTrack.isEmpty() -> {
                                state.postValue(
                                    SearchScreenState.Empty
                                )
                            }
                            else -> {
                                state.postValue(SearchScreenState.Content(foundTrack))
                            }
                        }
                    }
                }
            )
        }

    }

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { loadData(changedText) }

        handler.postDelayed(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    fun onTrackClick(track: Track) {
        searchHistoryInteractor.apply {
            addTrackToHistory(track)
        }
        val json = getJsonFromTrackUseCase.execute(track)
        trackClickEvent.postValue(json)
    }
    fun showHistory() {
        val trackList = searchHistoryInteractor.read()
        state.postValue(
            SearchScreenState.SearchHistoryContent(trackList)
        )
    }

    fun clearSearchHistory() {
        val trackList = ArrayList<Track>()
        searchHistoryInteractor.clear()
        state.postValue(
            SearchScreenState.SearchHistoryContent(trackList)
        )
    }
}