package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchTracks(searchQuery: String) : Flow<Pair<List<TrackSearch>?, Boolean?>>
    suspend fun getTracksHistory(consumer: HistoryConsumer)
    suspend fun addTrackToHistory(track: TrackSearch)
    suspend fun clearHistory()
    interface HistoryConsumer {
        fun consume(tracks: List<TrackSearch>?)
    }
}