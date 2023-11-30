package com.example.playlistmaker.search.domain

sealed class TrackSearchScreenState {
    object Loading: TrackSearchScreenState()
    data class Content(
        val trackModel: TrackSearch,
    ): TrackSearchScreenState()

}