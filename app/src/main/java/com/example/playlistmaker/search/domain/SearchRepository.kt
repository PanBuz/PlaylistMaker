package com.example.playlistmaker.search.domain
import kotlinx.coroutines.flow.Flow
interface SearchRepository {
    suspend fun searchTrack(searchQuery: String): Flow< ResponseStatus<List<TrackSearch>>>
    fun getTrackHistoryList(): List<TrackSearch>
    fun addTrackInHistory(track: TrackSearch)
    fun clearHistory()

}