package com.example.playlistmaker.data.search.impl

import android.app.Application
import com.example.playlistmaker.util.SEARCH_HISTORY
import com.example.playlistmaker.data.GetSharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.api.SearchHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryImpl(application: Application): SearchHistory {

    private val sharedPrefs = GetSharedPreferences().execute(application)

    private fun write(trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    override fun read(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null)
        return if ( json == null) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson(json, type)
        }
    }

    override fun addTrackToHistory(track: Track) {
        val trackSearchHistory = read()
        if (trackSearchHistory.contains(track)) trackSearchHistory.remove(track)
        if (trackSearchHistory.size > 9) trackSearchHistory.removeAt(9)
        trackSearchHistory.add(0, track)
        write(trackSearchHistory)
    }

    override fun clear() {
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }
}