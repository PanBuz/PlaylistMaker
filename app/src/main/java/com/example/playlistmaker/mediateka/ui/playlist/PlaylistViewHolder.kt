package com.example.playlistmaker.mediateka.ui.playlist

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist

class PlaylistViewHolder (private val binding: LayoutPlaylistBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.tvTitle .text = playlist.name
        binding.tvCount.text = choiceOfWord(playlist.countTracks)
        Log.d ("PAN_PlaylistViewHolder", "imagePl = ${playlist.image}")
        Glide.with(itemView)
            .load(playlist.image)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.ivCover)
    }
    private fun choiceOfWord(countTracks: Int): String {

        val s = when (countTracks % 10) {
            1 -> "$countTracks трек"
            2, 3, 4 -> "$countTracks трека"
            else -> "$countTracks треков"
        }
        return s
    }
}