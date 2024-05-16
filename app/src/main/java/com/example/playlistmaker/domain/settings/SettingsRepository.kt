package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsRepository {
    fun getSettingTheme(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}