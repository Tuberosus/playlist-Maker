package com.example.playlistmaker.ui.audioPlayer.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class TrackToPlayListAdapter : RecyclerView.Adapter<TrackToPlaylistViewHolder>() {

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
    }

}