package com.example.playlistmaker.search.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.StateSearch
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1500L
        var playedTracks = arrayListOf<TrackSearch>()

    }

    private val stateMutableLiveData = MutableLiveData<StateSearch>()
    fun stateLiveData(): LiveData<StateSearch> = stateMutableLiveData
    private var newSearchText: String? = null

    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY, viewModelScope, true
    ) { changedText ->
        searchSong(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (newSearchText != changedText) {
            newSearchText = changedText
            trackSearchDebounce(changedText)
        }

    }

    private fun searchSong(searchQuery: String) {
        Log.d("PAN_SearchViewModel", "Пришло на оправку searchSong ($searchQuery)")

        if (searchQuery.isNotEmpty()) {
            updateState(StateSearch.Loading)

            viewModelScope.launch {
                searchInteractor.searchTracks(searchQuery)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(searchTracks: List<TrackSearch>?, errorMessage: Boolean?) {
        val tracks = arrayListOf<TrackSearch>()
        if (searchTracks != null) {
            playedTracks.addAll(searchTracks)
            tracks.addAll(searchTracks)
        }

        when {
            errorMessage != null -> {
                updateState(StateSearch.Empty())
            }

            tracks.isEmpty() -> {
                updateState(StateSearch.Content(tracks))
            }

            else -> {
                updateState(StateSearch.Content(tracks))
            }
        }
    }


    fun getTracksHistory() {
        viewModelScope.launch {
            searchInteractor.getTracksHistory(object : SearchInteractor.HistoryConsumer {
                override fun consume(tracks: List<TrackSearch>?) {
                    if (tracks.isNullOrEmpty()) {
                        updateState(StateSearch.EmptyHistoryList())
                    } else {
                        updateState(StateSearch.ContentHistoryList(tracks))
                    }
                }
            })
        }
    }

    fun addTrackToHistory(track: TrackSearch) {
        viewModelScope.launch { searchInteractor.addTrackToHistory(track) }
    }

    fun clearHistory() {
        viewModelScope.launch { searchInteractor.clearHistory() }
        updateState(StateSearch.EmptyHistoryList())
    }

    private fun updateState(state: StateSearch) {
        stateMutableLiveData.postValue(state)
    }

}


