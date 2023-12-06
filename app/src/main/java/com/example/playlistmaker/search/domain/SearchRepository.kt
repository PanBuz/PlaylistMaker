package com.example.playlistmaker.search.domain

interface SearchRepository {
    fun searchTrack(expression: String): ResponseStatus<List<TrackSearch>>
    fun getTrackHistoryList(): List<TrackSearch>
    fun addTrackInHistory(track: TrackSearch)
    fun clearHistory()

}