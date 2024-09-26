package com.example.playlistmaker.ui.media.fragments.playlists

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.PlaylistScreenState
import com.example.playlistmaker.ui.media.fragments.playlistItem.PlaylistItemFragment
import com.example.playlistmaker.ui.media.view_model.PlaylistsViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistsFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistAdapter

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

        onPlaylistClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            openPlaylist(playlist)
        }

        adapter = PlaylistAdapter { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            view.resources.getDimension(R.dimen.setting_title_margin),
            view.resources.displayMetrics
        ).toInt()
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dp, false))

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlaylistFragment)
        }

        viewModel.playlistObserver.observe(viewLifecycleOwner) { state ->
            renderScreenState(state)
        }

        /////

        var s = ""
        File(
            requireActivity().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), "playlist_album"
        ).listFiles()?.forEach {
            s += "\n$it"
        }
        Log.d("MyTag", s)
        /////
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
            is PlaylistScreenState.Empty -> {
                Log.d("MyTag", "empty")
                showEmpty() }
            is PlaylistScreenState.Loading -> {
                Log.d("MyTag", "loading")
                showLoading() }
            is PlaylistScreenState.Content -> {
                Log.d("MyTag", "content")
                showContent(state.playlists) }
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

    private fun openPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistItemFragment,
            PlaylistItemFragment.createArgs(playlist.id)
        )
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