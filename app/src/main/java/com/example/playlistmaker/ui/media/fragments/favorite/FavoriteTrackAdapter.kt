package com.example.playlistmaker.ui.media.fragments.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class FavoriteTrackAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<FavoriteTrackVewHolder>() {
    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackVewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return FavoriteTrackVewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: FavoriteTrackVewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            //переход в аудиоплеер
            clickListener.onTrackClick(trackList[position])
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}