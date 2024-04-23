package com.example.playlistmaker.ui.audioPlayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_TAG = "track"
    }
    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val playerInteractor = Creator.provideMediaPlayerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // кнопка назад
        binding.buttonBack.setOnClickListener { finish() }

        runnable = runSongTimer()
        handler = Handler(Looper.getMainLooper())

        // получение выбранного трека
        val jsonTrack = intent.getStringExtra(TRACK_TAG)
        track = Creator.provideGetTrackUseCase().execute(jsonTrack!!)

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

        //загрузка трека в медиаплеер
        if (!track.previewUrl.isNullOrBlank()) {
            playerInteractor.preparePlayer(
                url = track.previewUrl!!,
                callback = {
                    handler.removeCallbacks(runnable)
                    binding.buttonPlay.setImageResource(R.drawable.button_play)
                    binding.currentDuration.text = getText(R.string.current_duration)
                    }
                )
        }

        //обработка нажатия кнопки play
        binding.buttonPlay.setOnClickListener { playbackControl() }

    }

    private fun playbackControl() {
        when (playerInteractor.getState()) {
            PlayerState.PLAYING -> pause()
            PlayerState.PAUSED -> play()
            PlayerState.PREPARED -> play()
            else -> null
        }
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
        handler.removeCallbacks(runnable)
    }

    private fun play() {
        playerInteractor.play {
            binding.buttonPlay.setImageResource(R.drawable.button_pause)
            handler.post(runnable)
        }
    }

    private fun pause() {
        playerInteractor.pause {
            binding.buttonPlay.setImageResource(R.drawable.button_play)
            handler.removeCallbacks(runnable)
        }
    }

    private fun runSongTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.currentDuration.text = SimpleDateFormat("mm:ss",
                    Locale.getDefault()).format(
                    playerInteractor.getCurrentPosition())

                handler.postDelayed(this, 100)
            }

        }
    }
}