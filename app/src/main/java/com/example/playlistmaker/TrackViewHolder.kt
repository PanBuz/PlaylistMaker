package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTimeTrack)
    private val ivTrackImage: ImageView = itemView.findViewById(R.id.ivPlaceHolderImage)

    private val radiusIconImage = 4

    fun bind(item: Track) {
        Glide.with(ivTrackImage).load(item.artworkUrl100)
            .centerInside()
            .transform(RoundedCorners(radiusIconImage))
            .placeholder(R.drawable.placeholder)
            .into(ivTrackImage)


        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text = item.trackTime
    }
}