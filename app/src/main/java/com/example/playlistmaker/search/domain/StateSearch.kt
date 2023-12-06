package com.example.playlistmaker.search.domain

sealed interface StateSearch  {
    data class ContentHistoryList(val historyList: List<TrackSearch>) : StateSearch
    data class Content(val tracks: List<TrackSearch>) : StateSearch
    object Loading : StateSearch
    class Error : StateSearch
    class Empty : StateSearch
    class EmptyHistoryList() : StateSearch

}