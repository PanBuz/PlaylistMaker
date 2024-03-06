package com.example.playlistmaker.search.domain


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override suspend fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }

    override suspend fun addTrackToHistory(track: TrackSearch) {
        repository.addTrackToHistory(track)
    }

    override suspend fun clearHistory() {
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
