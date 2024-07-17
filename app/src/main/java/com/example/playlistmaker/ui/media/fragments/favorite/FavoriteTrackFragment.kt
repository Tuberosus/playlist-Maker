package com.example.playlistmaker.ui.media.fragments.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentFavoriteTrackBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.media.MediaScreenState
import com.example.playlistmaker.ui.media.view_model.FavoriteTrackViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTrackFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<FavoriteTrackViewModel>()

    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteTrackAdapter

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteTrackAdapter { track ->
            onTrackClickDebounce(track)
        }

        binding.favoriteTrack.adapter = adapter

        viewModel.stateObserver.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) { track ->
            openPlayer(track)
        }

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.onTrackClick(track)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.showFavorite()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderState(state: MediaScreenState) {
        when (state) {
            is MediaScreenState.Loading -> loading()
            is MediaScreenState.Content -> showContent(state.tracks)
            is MediaScreenState.Empty -> showEmpty()
        }
    }

    private fun loading() {
        binding.favoriteTrack.visibility = View.GONE
        binding.favoritePlaceholder.visibility = View.GONE
        binding.favoriteEmptyText.visibility = View.GONE
        binding.favoriteProgressbar.visibility = View.VISIBLE
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        binding.favoriteTrack.visibility = View.VISIBLE
        binding.favoritePlaceholder.visibility = View.GONE
        binding.favoriteEmptyText.visibility = View.GONE
        binding.favoriteProgressbar.visibility = View.GONE

        adapter.trackList.clear()
        adapter.trackList.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.favoriteTrack.visibility = View.GONE
        binding.favoritePlaceholder.visibility = View.VISIBLE
        binding.favoriteEmptyText.visibility = View.VISIBLE
        binding.favoriteProgressbar.visibility = View.GONE
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun openPlayer(jsonTrack: String) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(AudioPlayerActivity.TRACK_TAG, jsonTrack)
        startActivity(intent)
    }
}