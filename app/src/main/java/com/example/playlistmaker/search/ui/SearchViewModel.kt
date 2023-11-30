package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.StateSearch
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.App
class SearchViewModel (
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1500L
        /*fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SearchViewModel(
                    searchInteractor = Creator.provideSearchInteractor(app.applicationContext)
                )
            }
        }*/
    }

    private val handler = Handler(Looper.getMainLooper()) // нужен только для дебаусе
    lateinit var searchRunnable: Runnable
    private val stateMutableLiveData = MutableLiveData<StateSearch>()
    private var newSearchText: String? = null

    init {
        Log.d("PAN_SearchViewModel", "VM Search onCreate")
    }
    fun stateLiveData(): LiveData<StateSearch> = stateMutableLiveData

    fun searchDebounce(changedText: String, hasError: Boolean) {
        var searchedText = ""
        if (newSearchText == changedText && !hasError) {
            return
        }
        searchedText = changedText
        this.newSearchText = changedText
        handler.removeCallbacksAndMessages("PANBUZ")
        searchRunnable = Runnable { searchSong(searchedText) }
        handler.postDelayed(searchRunnable, "PANBUZ", SEARCH_DEBOUNCE_DELAY)
    }
    private fun searchSong(expression: String) {
        Log.d("PAN_SearchViewModel", "Пришло на оправку searchSong ($expression)")

        if (expression.isNotEmpty()) {
            updateState(StateSearch.Loading)

            searchInteractor.searchTracks(expression, object : SearchInteractor.SearchConsumer {
                override fun consume(searchTracks: List<TrackSearch>?, hasError: Boolean?) {
                    val tracks = arrayListOf<TrackSearch>()

                    if (searchTracks != null) {
                        tracks.addAll(searchTracks)

                        when {
                            tracks.isEmpty() -> {
                                updateState(StateSearch.Empty())
                            }

                            tracks.isNotEmpty() -> {
                                updateState(StateSearch.Content(tracks))
                            }
                        }
                    } else {
                        updateState(StateSearch.Error())
                    }
                }
            })
        }
    }

    fun getTracksHistory() {
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

    fun addTrackToHistory(track: TrackSearch) {
        searchInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    private fun updateState(state: StateSearch) {
        stateMutableLiveData.postValue(state)
    }

    override fun onCleared() {
        Log.d("PAN_SearchViewModel", "VM Search onCleared")
        handler.removeCallbacks(searchRunnable)
    }

}
