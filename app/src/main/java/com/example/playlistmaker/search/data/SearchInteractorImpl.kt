package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.ResponseStatus
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }

    override fun addTrackToHistory(track: TrackSearch) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override suspend fun searchTracks(searchQuery: String): Flow<Pair<List<TrackSearch>?, Boolean?>>
    {
        return repository.searchTrack(searchQuery).map { result ->
            when (result) {
                is ResponseStatus.Success -> {
                    Pair(result.data, null)
                }

                is ResponseStatus.Error<*> -> {
                    Pair(null, result.hasError)
                }
            }
        }

    }
}
