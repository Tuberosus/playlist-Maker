package com.example.playlistmaker.ui.media.fragments.playlistItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class PlaylistItemAdapter (
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<PlaylistItemViewHolder>() {
    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.isLongClickable = true

        holder.itemView.setOnClickListener {
            //переход в аудиоплеер
            clickListener.onTrackClick(trackList[position])
        }

        holder.itemView.setOnLongClickListener {
            clickListener.onTrackLongClick(trackList[position])
            return@setOnLongClickListener true
        }
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)
        fun onTrackLongClick(track: Track)
    }
}