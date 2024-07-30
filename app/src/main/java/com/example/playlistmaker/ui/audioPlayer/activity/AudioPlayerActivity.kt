package com.example.playlistmaker.ui.audioPlayer.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.BottomSheetScreenState
import com.example.playlistmaker.ui.audioPlayer.PlaybackState
import com.example.playlistmaker.ui.audioPlayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.fragments.playlists.AddPlaylistFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_TAG = "track"
    }

    private val jsonTrack by lazy { intent.getStringExtra(TRACK_TAG) ?: "" }

    private val viewModel by viewModel<AudioPlayerViewModel> {
        parametersOf(jsonTrack)
    }

    private lateinit var adapter: TrackToPlayListAdapter

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TrackToPlayListAdapter()
        binding.bottomRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.bottomRecyclerView.adapter = adapter

        //загрузка экрана
        viewModel.getScreenStateLiveData().observe(this) { track ->
            loadTrackInfo(track)
        }

        //работа с воспроизведением
        viewModel.getPlaybackStateLiveData().observe(this) { playbackState ->
            when (playbackState) {
                is PlaybackState.Play -> {
                    play(playbackState.time)
                }

                PlaybackState.Pause -> {
                    pause()
                }

                PlaybackState.Default -> {
                    setDefaultPlayerState()
                }
            }
        }

        // Загрузка плейлистов в bottom sheet
        viewModel.getBottomSheetState().observe(this) { bottomSheetState ->
            when (bottomSheetState) {
                is BottomSheetScreenState.Loading -> { showPlaylistLoading() }
                is BottomSheetScreenState.Content -> { showPlaylist(bottomSheetState.playlists) }
            }
        }

        // Загрузка есть ли лайк у трека
        viewModel.getIsLikeLiveData().observe(this) { isLike ->
            setLike(isLike)
        }

        // кнопка назад
        binding.buttonBack.setOnClickListener { finish() }

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

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        pause()
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
            currentDuration.text = "00:00" // заглушка время проигрывания
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

}