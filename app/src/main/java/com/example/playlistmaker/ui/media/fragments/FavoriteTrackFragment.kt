package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTrackBinding

class FavoriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTrackFragment()
    }

    private lateinit var binding: FragmentFavoriteTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }
}