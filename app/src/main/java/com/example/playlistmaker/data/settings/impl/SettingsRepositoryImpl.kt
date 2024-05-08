package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository{

    private companion object {
        const val DARK_THEME = "dark_theme"
    }

    override fun getSettingTheme(): ThemeSettings {
        val darkTheme = sharedPreferences.getBoolean(DARK_THEME, false)
        return ThemeSettings(isDark = darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, settings.isDark)
            .apply()
    }


}