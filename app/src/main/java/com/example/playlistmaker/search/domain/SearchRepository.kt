package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus
interface SearchRepository {
    fun searchTrack(expression: String):  ResponseStatus<List<TrackSearch>>
    fun getTrackHistoryList(): List<TrackSearch>
    fun addTrackInHistory(track: TrackSearch)
    fun clearHistory()
}