package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(track: Track) {
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .centerInside()
            .placeholder(R.drawable.ic_album_default)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        itemView.resources.getDimension(R.dimen.artwork_radius),
                        itemView.resources.displayMetrics).toInt()
                )
            )
            .into(artwork)
        trackName.text = track.trackName
        artistName.text = track.artistName.trim()
        trackTime.text = SimpleDateFormat("mm:ss",
                            Locale.getDefault()).format(track.trackTimeMillis.toInt())
    }
}