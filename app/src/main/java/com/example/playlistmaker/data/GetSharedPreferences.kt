package com.example.playlistmaker.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class GetSharedPreferences {
        private companion object {
            const val SETTING_PREFERENCES = "setting_preferences"
        }
    fun execute(application: Application): SharedPreferences {
        return application.getSharedPreferences(SETTING_PREFERENCES, Context.MODE_PRIVATE)
    }
}