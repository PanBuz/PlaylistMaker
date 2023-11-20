package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.TrackSearch

fun interface TrackClickListener {
    fun onClickRecyclerItemView (track: TrackSearch)
}