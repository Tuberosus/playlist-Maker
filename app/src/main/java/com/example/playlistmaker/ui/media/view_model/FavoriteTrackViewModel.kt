package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.GetJsonFromTrackUseCase
import com.example.playlistmaker.ui.media.MediaScreenState
import com.example.playlistmaker.util.SingleEventLiveData
import kotlinx.coroutines.launch

class FavoriteTrackViewModel(
    private val interactor: FavoriteTrackInteractor,
    private val getJsonFromTrackUseCase: GetJsonFromTrackUseCase
) : ViewModel() {

    private val liveDataState = MutableLiveData<MediaScreenState>()
    val stateObserver: LiveData<MediaScreenState> = liveDataState

    init {
        showFavorite()
    }

    private val trackClickEvent = SingleEventLiveData<String>()
    fun getTrackClickEvent(): LiveData<String> = trackClickEvent

    fun showFavorite() {
        renderState(MediaScreenState.Loading)
        viewModelScope.launch {
                interactor.showFavoriteTracks()
                    .collect { tracks ->
                        processResult(tracks)
                    }
        }
    }

    fun onTrackClick(track: Track) {
        val json = getJsonFromTrackUseCase.execute(track)
        trackClickEvent.postValue(json)
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(MediaScreenState.Empty)
        } else {
            renderState(MediaScreenState.Content(tracks))
        }
    }

    private fun renderState(state: MediaScreenState) {
        liveDataState.postValue(state)
    }

}