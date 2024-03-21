package com.example.playlistmaker

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // кнопка назад
        binding.buttonBack.setOnClickListener { finish() }

        // получение выбранного трека
        val jsonTrack = intent.getStringExtra(TrackAdapter.trackTag)
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
            currentDuration.text = "0:30" // заглушка время проигрывания
            duration.text =  track.trackTime
            album.text = track.collectionName
            year.text = track.releaseDate.substring(0,4)
            genre.text = track.primaryGenreName
            country.text = track.country
        }
    }
}