package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Track

class ClickedMusicAdapter (
    private val clickedSearchSongs: MutableList<Track>,
    private val listener : TrackAdapter.Listener
    ) : RecyclerView.Adapter <TrackViewHolder> ()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder (LayoutInflater
            .from(parent.context)
            .inflate(R.layout.treck_item, parent, false))
    }

    override fun getItemCount(): Int {
        return clickedSearchSongs.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(clickedSearchSongs[position], listener)
    }


}