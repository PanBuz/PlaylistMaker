package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun favoriteTracks(): Flow<ArrayList<TrackSearch>>
    fun setClickedTrack(track: TrackSearch)
}