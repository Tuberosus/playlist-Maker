package com.example.playlistmaker.ui.audioPlayer.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class TrackToPlayListAdapter(private val clickListener: PlaylistClickListener) : RecyclerView.Adapter<TrackToPlaylistViewHolder>() {

    val playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackToPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_choice, parent, false)
        return TrackToPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: TrackToPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])

        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlists[position])
        }

    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }

}