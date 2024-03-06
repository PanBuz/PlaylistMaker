package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchTracks(searchQuery: String) : Flow<Pair<List<TrackSearch>?, Boolean?>>
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