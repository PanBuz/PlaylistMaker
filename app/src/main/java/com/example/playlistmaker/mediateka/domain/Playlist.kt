package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.TrackSearch

data class Playlist(
    val id: Int,
    val name: String,
    val descript: String,
    val image: String = "",
    val tracks: ArrayList<TrackSearch> = arrayListOf(),
    var countTracks: Int = 0,
    val timePl : Long = 0
)