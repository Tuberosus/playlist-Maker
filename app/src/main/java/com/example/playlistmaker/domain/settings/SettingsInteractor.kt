package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.models.ThemeSettings

interface SettingsInteractor {
    fun getSettingTheme(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}