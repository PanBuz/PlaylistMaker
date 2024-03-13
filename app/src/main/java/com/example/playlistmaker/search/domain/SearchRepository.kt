package com.example.playlistmaker.search.domain
import kotlinx.coroutines.flow.Flow
interface SearchRepository {
    suspend fun searchTrack(searchQuery: String): Flow< ResponseStatus<List<TrackSearch>>>
    suspend fun getTrackHistoryList(): List<TrackSearch>
    suspend fun addTrackToHistory(track: TrackSearch)
    suspend fun clearHistory()

}