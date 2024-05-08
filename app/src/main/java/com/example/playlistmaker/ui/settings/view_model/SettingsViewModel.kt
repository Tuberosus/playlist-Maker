package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Utils.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val sharingInteractor = this[APPLICATION_KEY]?.applicationContext?.let {
                    Creator.provideSharingInteractor(it)
                }

                val settingsInteractor = this[APPLICATION_KEY]?.applicationContext?.let {
                    Creator.providerSettingsInteractor(it)
                }

                SettingsViewModel(
                    sharingInteractor!!,
                    settingsInteractor!!,
                )
            }
        }
    }

    //Sharing
    private val sharingLiveData = MutableLiveData<Unit>()
    fun getSharingLiveData(): LiveData<Unit> = sharingLiveData


    fun openTerms() {
        sharingLiveData.value = sharingInteractor.openTerms()
    }

    fun sharingApp() {
        sharingLiveData.value = sharingInteractor.sharingApp()
    }

    fun openSupport() {
        sharingLiveData.value = sharingInteractor.openSupport()

    }

    //Settings
    private val settingsLiveData = MutableLiveData<ThemeSettings>()
    fun getSettingsLiveData(): LiveData<ThemeSettings> = settingsLiveData
    fun switchTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
        settingsLiveData.value = settingsInteractor.getSettingTheme()
    }
}