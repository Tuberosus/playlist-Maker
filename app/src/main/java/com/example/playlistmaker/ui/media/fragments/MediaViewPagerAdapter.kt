package com.example.playlistmaker.ui.media.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.fragments.favorite.FavoriteTrackFragment
import com.example.playlistmaker.ui.media.fragments.playlists.PlaylistsFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTrackFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}