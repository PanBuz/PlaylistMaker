package com.example.playlistmaker.mediateka.domain.favorite

import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun favoriteTracks(): Flow<List<TrackSearch>>
    fun setClickedTrack(track: TrackSearch)
}