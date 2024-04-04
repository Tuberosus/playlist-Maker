package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private lateinit var binding: ActivityAudioPlayerBinding

    private var playerState = STATE_DEFAULT
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // кнопка назад
        binding.buttonBack.setOnClickListener { finish() }

        runnable = runSongTimer()
        handler = Handler(Looper.getMainLooper())

        // получение выбранного трека
        val jsonTrack = intent.getStringExtra(SearchActivity.TRACK_TAG)
        val track = Gson().fromJson(jsonTrack, Track::class.java)

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

        //получение url аудио трека
        if (!track.previewUrl.isNullOrBlank()) {
            preparePlayer(track.previewUrl)
            binding.buttonPlay.setOnClickListener { playbackControl() }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { playerState = STATE_PREPARED }
            setOnCompletionListener {
                handler.removeCallbacks(runnable)
                playerState = STATE_PREPARED
                binding.buttonPlay.setImageResource(R.drawable.button_play)
                binding.currentDuration.text = getText(R.string.current_duration)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        handler.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun runSongTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.currentDuration.text = SimpleDateFormat("mm:ss",
                    Locale.getDefault()).format(mediaPlayer.currentPosition)

                handler.postDelayed(this, 100)
            }

        }
    }
}