package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.databinding.TreckItemBinding

class TrackAdapter(
    private val trackData: ArrayList<TrackSearch>,
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            TreckItemBinding.inflate(LayoutInflater
                        .from(parent.context), parent, false
                )
        )
    }
    override fun getItemCount() = trackData.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackData[position])
        holder.itemView.setOnClickListener { clickListener.onClickRecyclerItemView(trackData.get(position)) }
    }
}