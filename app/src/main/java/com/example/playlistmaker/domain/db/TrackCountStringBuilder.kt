package com.example.playlistmaker.domain.db

import android.app.Application
import android.content.Context
import com.example.playlistmaker.R

class TrackCountStringBuilder(private val context: Context) {

    fun build(count: Int): String {
        val mod10 = count % 10
        val mod100 = count % 100
        val form1 = "а"
        val form2 = "ов"
        val trackString = context.getString(R.string.track_string)
        return when {
            mod10 == 1 && mod100 != 11 -> "$count $trackString"
            mod10 in 2..4 && (mod100 !in 12..14) -> "$count $trackString$form1"
            else -> "$count $trackString$form2"
        }
    }
}