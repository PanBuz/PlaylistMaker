package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.utils.Converters

class PlayerViewHolder(private val binding: LayoutPlayerPlaylistBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist, imagePath: String) {
        binding.namePl.text = playlist.name
        binding.tvCountTracks.text = Converters(itemView.context).convertCountToTextTracks(playlist.countTracks)
        val imagePl = imagePath + "/" + DisplayPlaylistFragment.actualPlaylist!!.name + ".jpg"
        Glide.with(itemView)
            .load(imagePl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transform(RoundedCorners(8))
            .into(binding.imagePlaylist)

    }
}