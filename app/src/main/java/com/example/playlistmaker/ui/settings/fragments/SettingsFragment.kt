package com.example.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.util.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.themeSwitcher.isChecked = (requireActivity().applicationContext as App).darkTheme

        settingsViewModel.getSettingsLiveData().observe(viewLifecycleOwner) {
            (requireActivity().applicationContext as App).switchTheme(it.isDark)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }

    }
}