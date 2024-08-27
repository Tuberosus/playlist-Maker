package com.example.playlistmaker.ui.media.fragments.playlists

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.util.TrackCountStringBuilder
import java.io.File


class PlaylistViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val playlistImage = itemView.findViewById<ImageView>(R.id.playlistImage)
    private val playlistName = itemView.findViewById<TextView>(R.id.playlistName)
    private val countTrack = itemView.findViewById<TextView>(R.id.count)

    fun bind(playlist: Playlist) {
        val cornerSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            itemView.resources.getDimension(R.dimen.playlist_image_radius),
            itemView.resources.displayMetrics).toInt()
        Glide.with(itemView.context)
            .load(File(playlist.imageDir ?: ""))
            .placeholder(R.drawable.placeholder_playlist_default)

//            .fitCenter()
            .transform(
                RoundedCorners(cornerSize)
            )
            .into(playlistImage)

        playlistName.text = playlist.name

        val trackCountString = TrackCountStringBuilder(itemView.context).build(playlist.trackCount)
        countTrack.text = trackCountString
    }
}