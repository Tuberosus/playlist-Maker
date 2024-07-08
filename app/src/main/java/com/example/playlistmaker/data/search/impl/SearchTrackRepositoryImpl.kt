package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.SongsResponse
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient) : SearchTrackRepository {
    override fun getTrackList(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        emit(
            when (response.resultCode) {
                200 -> Resource.Success(
                    ArrayList(
                        (response as SongsResponse).results.map {
                            Track(
                                it.trackId,
                                it.trackName,
                                it.artistName,
                                it.trackTimeMillis,
                                it.artworkUrl100,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.previewUrl
                            )

                        })
                )

                else -> Resource.Error()
            }
        )
    }
}