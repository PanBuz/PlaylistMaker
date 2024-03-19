package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist

class PlayerViewHolder(private val binding: LayoutPlayerPlaylistBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.namePl.text = playlist.name
        binding.tvCountTracks.text = choiceOfWord(playlist.countTracks)
        Glide.with(itemView)
            .load(playlist.image)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(3))
            .into(binding.imagePlaylist)

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