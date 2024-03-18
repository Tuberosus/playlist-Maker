package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder> () {

    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            val sharedPrefs = holder.itemView.context.getSharedPreferences(SETTING_PREFERENCES,
                                                                            Context.MODE_PRIVATE)
            SearchHistory(sharedPrefs).apply {
                addTrackToHistory(trackList[position])
                write(trackSearchHistory)
            }

            //переход в аудиоплеер
            val displayIntent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
            val json = Gson().toJson(trackList[position])
            displayIntent.putExtra("track",json)
            holder.itemView.context.startActivity(displayIntent)
        }
    }
}