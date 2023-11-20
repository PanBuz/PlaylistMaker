package com.example.playlistmaker.search.domain

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: TrackSearch)
    fun clearHistory()
    interface SearchConsumer {
        fun consume(tracks: List<TrackSearch>?, hasError: Boolean?)
    }
    interface HistoryConsumer {
        fun consume(tracks: List<TrackSearch>?)
    }
}