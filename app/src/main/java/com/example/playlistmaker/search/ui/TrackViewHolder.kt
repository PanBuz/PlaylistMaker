package com.example.playlistmaker.search.ui


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TreckItemBinding
import com.example.playlistmaker.search.domain.TrackSearch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TreckItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemTrack: TrackSearch) {
        Glide.with(binding.ivPlaceHolderImage).load(itemTrack.artworkUrl100)
            .centerInside()
            .error(R.drawable.error_mode)
            .transform(RoundedCorners(4))
            .placeholder(R.drawable.placeholder)
            .into(binding.ivPlaceHolderImage)

        binding.tvTrackName.text = itemTrack.trackName
        binding.tvArtistName.text = itemTrack.artistName
        binding.tvTimeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(itemTrack.trackTimeMillis)
    }
}