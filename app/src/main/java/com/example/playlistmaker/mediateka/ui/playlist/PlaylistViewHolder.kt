package com.example.playlistmaker.mediateka.ui.playlist

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.utils.Converters

class PlaylistViewHolder (private val binding: LayoutPlaylistBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist, imagePath: String) {
        binding.tvTitle .text = playlist.name
        binding.tvCount.text = Converters(itemView.context).convertCountToTextTracks (playlist.countTracks)
        val coverPl = imagePath + "/" + playlist.name + ".jpg"
        Log.d ("PAN_PlaylistViewHolder", "imagePl = ${ coverPl}")
        Glide.with(itemView)
            .load(coverPl )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(30))
            .into(binding.ivCover)
    }
}