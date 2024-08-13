package com.example.playlistmaker.ui.audioPlayer.fragment

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.AudioPlayerScreenState
import com.example.playlistmaker.ui.audioPlayer.BottomSheetScreenState
import com.example.playlistmaker.ui.audioPlayer.ToastState
import com.example.playlistmaker.ui.audioPlayer.view_model.AudioPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    companion object {
        const val TRACK_TAG = "track"

        fun createArgs(jsonTrack: String): Bundle {
            return bundleOf(
                TRACK_TAG to jsonTrack
            )
        }
    }

    private val jsonTrack by lazy { requireArguments().getString(TRACK_TAG) }
    private val viewModel by viewModel<AudioPlayerViewModel> {
        parametersOf(jsonTrack)
    }

    private lateinit var adapter: TrackToPlayListAdapter

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.bottomSheetContainer)
    }

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackToPlayListAdapter{
            viewModel.putTrackIntoPlaylist(it)
        }

        binding.bottomRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.bottomRecyclerView.adapter = adapter

        viewModel.getAudioPlayerState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        // Загрузка плейлистов в bottom sheet
        viewModel.getBottomSheetState().observe(viewLifecycleOwner) { bottomSheetState ->
            when (bottomSheetState) {
                is BottomSheetScreenState.Loading -> {
                    showPlaylistLoading()
                }

                is BottomSheetScreenState.Content -> {
                    showPlaylist(bottomSheetState.playlists)
                }
            }
        }

        // обработка toast при добавлении трека в плейлист
        viewModel.getToastTextLiveData().observe(viewLifecycleOwner) { state ->
            showToast(state)
        }

        // кнопка назад
        binding.buttonBack.setOnClickListener { findNavController().popBackStack() }

        //обработка нажатия кнопки play
        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonLike.setOnClickListener {
            viewModel.clickOnLike()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_addPlaylistFragment)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyTag", "onResume")
        loadTrackInfo(viewModel.track)
        viewModel.getPlaylistState()
    }

    private fun render (state: AudioPlayerScreenState) {
        when (state) {
            is AudioPlayerScreenState.LoadTrack -> loadTrackInfo(state.track)
            is AudioPlayerScreenState.Play -> play(state.time)
            is AudioPlayerScreenState.Pause -> pause()
            is AudioPlayerScreenState.Default -> setDefaultPlayerState()
            is AudioPlayerScreenState.TrackLike -> setLike(state.isLike)
        }
    }

    private fun loadTrackInfo(track: Track) {
        //загрузка фото альбома
        Glide.with(this)
            .load(track.artworkUrl512)
            .centerInside()
            .placeholder(R.drawable.ic_album_default)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.artwork_radius),
                        resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.artworkUrl100)

        //Заполнение инфо трека
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            currentDuration.text = viewModel.getCurrentPosition() //"00:00" // заглушка время проигрывания
            duration.text = track.trackTime
            album.text = track.collectionName
            year.text = track.releaseDate.substring(0, 4)
            genre.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun play(time: String) {
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
        binding.currentDuration.text = time
    }

    private fun pause() {
        binding.buttonPlay.setImageResource(R.drawable.button_play)
    }

    private fun setDefaultPlayerState() {
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        binding.currentDuration.text = getText(R.string.current_duration)
    }

    private fun setLike(isLike: Boolean) {
        binding.buttonLike.setImageResource(
            if (isLike) {
                R.drawable.button_like_active
            } else {
                R.drawable.button_like_inactive
            }
        )
    }

    private fun showPlaylistLoading() {
        binding.bottomRecyclerView.visibility = View.GONE
        binding.searchProgressbar.visibility = View.VISIBLE
    }

    private fun showPlaylist(playlists: List<Playlist>) {
        binding.bottomRecyclerView.visibility = View.VISIBLE
        binding.searchProgressbar.visibility = View.GONE

        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(state: ToastState) {
        when (state) {
            is ToastState.ShowSuccess -> {
                Toast.makeText(requireContext(), state.text, Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.toastWasShown()
            }
            is ToastState.ShowError -> {
                Toast.makeText(requireContext(), state.text, Toast.LENGTH_SHORT).show()
                viewModel.toastWasShown()
            }
            is ToastState.None -> null
        }
    }

}