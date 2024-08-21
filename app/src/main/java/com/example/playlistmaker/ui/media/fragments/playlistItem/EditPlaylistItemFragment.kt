package com.example.playlistmaker.ui.media.fragments.playlistItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.ui.media.EditPlaylistScreenState
import com.example.playlistmaker.ui.media.fragments.playlists.AddPlaylistFragment
import com.example.playlistmaker.ui.media.view_model.EditPlaylistItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlaylistItemFragment : AddPlaylistFragment() {

    companion object {
        private const val FILE_PATH = "file_path"
        private const val PLAYLIST_NAME = "playlist_name"
        private const val PLAYLIST_DESCRIPTION = "playlist_description"
        fun createArgs(
            filePath: String?,
            playlistName: String,
            playlistDescription: String?
        ): Bundle {
            return bundleOf(
                FILE_PATH to filePath,
                PLAYLIST_NAME to playlistName,
                PLAYLIST_DESCRIPTION to playlistDescription
            )
        }
    }

    private val filePath by lazy { requireArguments().getString(FILE_PATH) }
    private val playlistName by lazy { requireArguments().getString(PLAYLIST_NAME) }
    private val playlistDescription by lazy { requireArguments().getString(PLAYLIST_DESCRIPTION) }

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    override val viewModel by viewModel<EditPlaylistItemViewModel> {
        parametersOf(filePath, playlistName, playlistDescription)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = getString(R.string.edit)

        viewModel.getPlaylistInfoObserver().observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditPlaylistScreenState.PlaylistInfo -> showPlaylistInfo(state)
            }
        }

    }

    private fun showPlaylistInfo(state: EditPlaylistScreenState.PlaylistInfo) {
        if (!state.filePath.isNullOrEmpty()) {
            binding.addPhoto.setImageURI(File(filePath).toUri())
        }

        binding.nameInputEditText.setText(state.playlistName)

        if (!state.playlistDescription.isNullOrEmpty()) {
            binding.descriptionInputEditText.setText(state.playlistDescription)
        }
    }
}
