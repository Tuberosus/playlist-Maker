package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.Utils.Resource
import com.example.playlistmaker.domain.search.api.SearchTrackRepository
import com.example.playlistmaker.domain.search.api.SearchTrackInteractor
import java.util.concurrent.Executors

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository):
    SearchTrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(expression: String, consumer: SearchTrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.getTrackList(expression)) {
                is Resource.Success -> { consumer.consume(resource.data) }
                is Resource.Error -> { consumer.consume(null) }
            }
//            consumer.consume(repository.getTrackList(expression))
        }
    }
}