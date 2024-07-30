package com.example.playlistmaker.ui.media.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.PlaylistScreenState
import com.example.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlaylistFragment)
        }

        viewModel.playlistObserver.observe(viewLifecycleOwner) { state ->
            renderScreenState(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun renderScreenState(state: PlaylistScreenState) {
        when(state) {
            is PlaylistScreenState.Empty -> { showEmpty() }
            is PlaylistScreenState.Loading -> { showLoading() }
            is PlaylistScreenState.Content -> { showContent(state.playlists) }
        }
    }

    private fun showEmpty() {
        visibleRecyclerView(false)
        visibleLoading(false)
        visiblePlaceholder(true)
    }

    private fun showLoading() {
        visiblePlaceholder(false)
        visibleRecyclerView(false)
        visibleLoading(true)
    }

    private fun showContent(playlists: List<Playlist>) {
        visiblePlaceholder(false)
        visibleLoading(false)
        visibleRecyclerView(true)

        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun visiblePlaceholder(isVisible: Boolean) {
        binding.placeholderImage.isVisible = isVisible
        binding.placeholderText.isVisible = isVisible
    }

    private fun visibleRecyclerView(isVisible: Boolean) {
        binding.recyclerView.isVisible = isVisible
    }

    private fun visibleLoading(isVisible: Boolean) {
        binding.searchProgressbar.isVisible = isVisible
    }
}