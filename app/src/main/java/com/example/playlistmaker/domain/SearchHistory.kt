package com.example.playlistmaker.domain

import android.content.SharedPreferences
import com.example.playlistmaker.SEARCH_HISTORY
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    var trackSearchHistory: ArrayList<Track> = ArrayList()
    fun write(trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun read(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null)
        return if ( json == null) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson(json, type)
        }
    }

    fun addTrackToHistory(track: Track) {
        trackSearchHistory = read()
        if (trackSearchHistory.contains(track)) trackSearchHistory.remove(track)
        if (trackSearchHistory.size > 9) trackSearchHistory.removeAt(9)
        trackSearchHistory.add(0, track)
    }

    fun clear() {
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }
}