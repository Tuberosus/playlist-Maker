package com.example.playlistmaker.ui.media.fragments.playlistItem

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistItemBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.fragment.AudioPlayerFragment
import com.example.playlistmaker.ui.media.PlaylistItemScreenState
import com.example.playlistmaker.ui.media.view_model.PlaylistItemViewModel
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistItemFragment : Fragment() {

    private val playlistId by lazy { requireArguments().getInt(PLAYLIST_TAG) }
    private val viewModel by viewModel<PlaylistItemViewModel> {
        parametersOf(playlistId)
    }

    private lateinit var playlist: Playlist

    private var _binding: FragmentPlaylistItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var adapter: PlaylistItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getScreenState(playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistItemAdapter(object : PlaylistItemAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                onTrackClickDebounce(track)
            }

            override fun onTrackLongClick(track: Track) {
                applyDeleteTrack(track)
            }

        })

        binding.recyclerView.adapter = adapter

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val json = viewModel.onTrackClick(track)
            openPlayer(json)
        }

        viewModel.screenStateObserver().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) { json ->
            openPlayer(json)
        }

        binding.shareIc.setOnClickListener {
            viewModel.sharePlaylist(playlistId)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainerSetting)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.settingIc.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.shareAdditionalMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist(playlistId)
        }

        binding.editAdditionalMenu.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistItemFragment_to_editPlaylistItemFragment,
                EditPlaylistItemFragment.createArgs(
                    playlist
                )
            )
        }

        binding.deleteAdditionalMenu.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.PopUpDialog)
                .setTitle(getString(R.string.delete_playlist_confirm, playlist.name))
                .setNegativeButton(R.string.negative) { dialog, which ->
                    // ничего не делаем
                }.setPositiveButton(R.string.positive) { dialog, which ->
                    // удаляем
                    findNavController().popBackStack()
                    viewModel.deletePlaylist(playlist)
                }.show()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun countBottomSheetHeight() {
        val view = binding.bottomSheetDelimiter

        view.post {
            // Values should no longer be 0
            val point = IntArray(2)
            view.getLocationOnScreen(point) // or getLocationInWindow(point)
            val (x, y) = point

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val trackListBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)
            trackListBehavior.peekHeight = displayHeight - y
        }
    }

    private fun render(state: PlaylistItemScreenState) {
        when (state) {
            is PlaylistItemScreenState.Loading -> {}
            is PlaylistItemScreenState.Content -> showPlaylistDetail(state)
            is PlaylistItemScreenState.EmptyShare -> showEmptyShareToast(state.toastTextId)
        }
    }

    private fun showPlaylistDetail(state: PlaylistItemScreenState.Content) {
        playlist = state.playlist
        val duration = state.duration
        val trackList = state.trackList
        val countTrack = resources.getQuantityString(
            R.plurals.track_plural_name,
            playlist.trackCount,
            playlist.trackCount
        )

        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()

        binding.emptyTracklistText.isVisible = trackList.isEmpty()

        Glide.with(this)
            .load(File(playlist.imageDir ?: ""))
            .placeholder(R.drawable.ic_album_default)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.image)

        binding.playlistDescription.isVisible = !playlist.description.isNullOrEmpty()

        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistCount.text = countTrack

        binding.playlistTime.text = resources.getQuantityString(
            R.plurals.minute_plural_name,
            duration.toInt(),
            duration.toInt()
        )

        setImageInSettingBottomSheet()
        binding.playlistNameInSetting.text = playlist.name
        binding.countInSetting.text = countTrack
        countBottomSheetHeight()
    }

    private fun setImageInSettingBottomSheet() {
        Glide.with(this)
            .load(File(playlist.imageDir ?: ""))
            .placeholder(R.drawable.ic_album_default)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.playlistImageInSetting)
    }

    private fun showEmptyShareToast(textId: Int) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(textId),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun openPlayer(jsonTrack: String) {
        findNavController().navigate(
            R.id.action_playlistItemFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(jsonTrack)
        )
    }

    private fun applyDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.PopUpDialog)
            .setMessage(R.string.track_delete_dialog_message)
            .setNegativeButton(R.string.negative) { dialog, which ->
                // ничего не делаем
            }.setPositiveButton(R.string.positive) { dialog, which ->
                // удаляем
                deleteTrackFromPlaylist(track)
            }.show()
    }

    private fun deleteTrackFromPlaylist(track: Track) {
        viewModel.deleteTrackFromPlaylist(track.trackId)
    }

    companion object {
        private const val PLAYLIST_TAG = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_TAG to playlistId)
        }
    }

}