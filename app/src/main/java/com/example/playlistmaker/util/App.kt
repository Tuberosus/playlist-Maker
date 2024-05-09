package com.example.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.GetSharedPreferences

const val DARK_THEME = "dark_theme"
const val SEARCH_HISTORY = "search_history"


class App: Application() {

    init {
        Creator.application = this
    }

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = GetSharedPreferences().execute(this)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}