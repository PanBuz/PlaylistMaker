package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.search.data.dto.ResponseStatus
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackSearch
import java.util.concurrent.Executors


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }
    override fun addTrackToHistory(track: TrackSearch) {
        repository.addTrackInHistory(track)
    }
    override fun clearHistory() {
        repository.clearHistory()
    }
    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            Log.d ("PAN_SearchInteractorIMPL", "Пришло на оправку searchTracks ($expression)")
            val resource = repository.searchTrack(expression)
            Log.d ("PAN_SearchInteractorIMPL", "Вернулось с Repository searchTracks (${resource.data.toString()})")
            when(resource) {
                is ResponseStatus.Success -> { consumer.consume(resource.data, false) }
                is ResponseStatus.Error -> { consumer.consume(null,  true) }
            }
        }
    }

}