package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.util.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val settingsViewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModel.getSettingsViewModelFactory()
        )[SettingsViewModel::class.java]

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageBackAction.setOnClickListener {
            finish()
        }

        binding.layoutShare.setOnClickListener {
            settingsViewModel.sharingApp()
        }

        binding.layoutSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.layoutUserAgreement.setOnClickListener {
            settingsViewModel.openTerms()
        }

        //Theme settings
        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme

        settingsViewModel.getSettingsLiveData().observe(this) {
            (applicationContext as App).switchTheme(it.isDark)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }


    }


}