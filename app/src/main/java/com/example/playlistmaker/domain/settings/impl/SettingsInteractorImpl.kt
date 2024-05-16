package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
): SettingsInteractor {

    override fun getSettingTheme(): ThemeSettings {
        return settingsRepository.getSettingTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}