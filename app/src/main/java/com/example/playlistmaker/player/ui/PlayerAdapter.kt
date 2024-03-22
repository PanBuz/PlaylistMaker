package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.ui.displayPlaylist.DisplayPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerAdapter(): RecyclerView.Adapter<PlayerViewHolder>(){



    var playlists = arrayListOf<Playlist>()
    var clickListener: ((Playlist) -> Unit)? = null
    lateinit var imagePath : String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return PlayerViewHolder(
            LayoutPlayerPlaylistBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }
    override fun getItemCount() = playlists.size
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playlists[position], imagePath)
        holder.itemView.setOnClickListener { clickListener?.invoke(playlists[position]) }
    }
}