package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.Utils.Resource
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.SongsResponse
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchTrackRepository

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient): SearchTrackRepository {
    override fun getTrackList(expression: String): Resource<ArrayList<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when(response.resultCode) {
            200 -> Resource.Success((response as SongsResponse).results)
            else -> Resource.Error()
        }
//        if (response.resultCode == 200) {
//            return (response as SongsResponse).results
//        } else {
//            return ArrayList()
//        }
    }
}