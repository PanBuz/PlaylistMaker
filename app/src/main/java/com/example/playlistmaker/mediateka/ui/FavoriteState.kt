package com.example.playlistmaker.mediateka.ui

import com.example.playlistmaker.search.domain.TrackSearch

sealed class FavoriteState {

    object Loading : FavoriteState()

    data class Content(
        val tracks: ArrayList<TrackSearch>
    ) : FavoriteState()

    data class Empty(
        val message: String
    ) : FavoriteState()
}