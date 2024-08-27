package com.example.playlistmaker.ui.media.fragments.playlistItem

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.media.EditPlaylistScreenState
import com.example.playlistmaker.ui.media.fragments.playlists.AddPlaylistFragment
import com.example.playlistmaker.ui.media.view_model.EditPlaylistItemViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlaylistItemFragment : AddPlaylistFragment() {

    companion object {
        private const val PLAYLIST = "playlist"

        fun createArgs(
            playlist: Playlist
        ): Bundle {
            return bundleOf(
                    PLAYLIST to playlist
            )
        }
    }

    private val playlist by lazy { requireArguments().getSerializable(PLAYLIST) }

    override val viewModel by viewModel<EditPlaylistItemViewModel> {
        parametersOf(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = getString(R.string.edit)
        binding.buttonSave.setText(R.string.save)

        viewModel.getPlaylistInfoObserver().observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditPlaylistScreenState.PlaylistInfo -> showPlaylistInfo(state)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }

            }
        )

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                launch {
                    viewModel.updatePlaylist()
                }.join()
                findNavController().popBackStack()
            }
        }

    }

    private fun showPlaylistInfo(state: EditPlaylistScreenState.PlaylistInfo) {
        if (!state.playlist.imageDir.isNullOrEmpty()) {
            binding.addPhoto.setImageURI(File(state.playlist.imageDir).toUri())
        }

        binding.nameInputEditText.setText(state.playlist.name)

        if (!state.playlist.description.isNullOrEmpty()) {
            binding.descriptionInputEditText.setText(state.playlist.description)
        }
    }
}
