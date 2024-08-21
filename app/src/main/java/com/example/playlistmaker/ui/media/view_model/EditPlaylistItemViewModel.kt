package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.media.api.PlaylistInteractor
import com.example.playlistmaker.ui.media.EditPlaylistScreenState

class EditPlaylistItemViewModel(
    interactor: PlaylistInteractor,
    override var imagePath: String?,
    override var playlistName: String?,
    override var playlistDescription: String?
) : AddPlaylistViewModel(interactor) {

    private val playlistInfoLiveData = MutableLiveData<EditPlaylistScreenState>()
    fun getPlaylistInfoObserver(): LiveData<EditPlaylistScreenState> = playlistInfoLiveData

    init {
        getPlaylistAttributes()
    }

    fun getPlaylistAttributes() {
        playlistInfoLiveData.value = EditPlaylistScreenState.PlaylistInfo(
            imagePath, playlistName, playlistDescription
        )
    }
}