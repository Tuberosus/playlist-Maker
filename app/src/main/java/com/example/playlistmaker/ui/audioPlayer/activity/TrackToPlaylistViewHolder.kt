package com.example.playlistmaker.ui.audioPlayer.activity

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.TrackCountStringBuilder
import com.example.playlistmaker.domain.models.Playlist

class TrackToPlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistImage = itemView.findViewById<ImageView>(R.id.playlistPicture)
    private val playListName = itemView.findViewById<TextView>(R.id.playListName)
    private val trackCount = itemView.findViewById<TextView>(R.id.trackCount)

    fun bind(playlist: Playlist) {
        Glide.with(itemView.context)
            .load(playlist.imageDir?.toUri())
            .placeholder(R.drawable.placeholder_playlist_default)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        itemView.resources.getDimension(R.dimen.artwork_radius),
                        itemView.resources.displayMetrics).toInt()
                )
            )
            .into(playlistImage)

        playListName.text = playlist.name
        trackCount.text = TrackCountStringBuilder(itemView.context).build(playlist.trackCount)
    }
}