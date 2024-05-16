package com.example.playlistmaker.ui.audioPlayer.activity

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.PlaybackState
import com.example.playlistmaker.ui.audioPlayer.view_model.AudioPlayerViewModel

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_TAG = "track"
    }

    private lateinit var viewModel:AudioPlayerViewModel

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonTrack = intent.getStringExtra(TRACK_TAG) ?: ""
        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]
        viewModel.loadPlayer(jsonTrack)

        //загрузка экрана
        viewModel.getScreenStateLiveData().observe(this) {track ->
            loadTrackInfo(track)
        }

        //работа с воспроизведением
        viewModel.getPlaybackStateLiveData().observe(this) {playbackState ->
            when(playbackState) {
                PlaybackState.Play -> { play() }
                PlaybackState.Pause -> { pause() }
                PlaybackState.Default -> { setDefaultPlayerState() }
                is PlaybackState.Timer -> { binding.currentDuration.text = playbackState.time}
            }
        }

        // кнопка назад
        binding.buttonBack.setOnClickListener { finish() }

        //обработка нажатия кнопки play
        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
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
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.artwork_radius),
                        resources.displayMetrics).toInt()
                )
            )
            .into(binding.artworkUrl100)

        //Заполнение инфо трека
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            currentDuration.text = "00:00" // заглушка время проигрывания
            duration.text =  track.trackTime
            album.text = track.collectionName
            year.text = track.releaseDate.substring(0,4)
            genre.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun play() {
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
    }

    private fun pause() {
        binding.buttonPlay.setImageResource(R.drawable.button_play)
    }

    private fun setDefaultPlayerState() {
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        binding.currentDuration.text = getText(R.string.current_duration)
    }

}