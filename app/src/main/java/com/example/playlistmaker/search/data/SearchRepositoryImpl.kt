package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.search.domain.ResponseStatus
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchDataStorage: SearchDataStorage
) : SearchRepository {


    override suspend fun searchTrack(searchQuery: String): Flow<ResponseStatus<List<TrackSearch>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(searchQuery))
        Log.d ("PAN_SearchRepositoryImpl", "Пришло в searchTrack_SearchRepository (${searchQuery})")

             when (response.resultCode) {
                -1 -> {
                    emit (ResponseStatus.Error())
                }

                 200 -> { with(response as TracksSearchResponse) {
                     val data = results.map {
                        TrackSearch(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName=it.artistName,
                            trackTimeMillis=it.trackTimeMillis,
                            artworkUrl100=it.artworkUrl100,
                            collectionName=it.collectionName,
                            releaseDate=it.releaseDate,
                            primaryGenreName=it.primaryGenreName,
                            country=it.country,
                            previewUrl=it.previewUrl
                        )
                    }
                     emit(ResponseStatus.Success(data))
                }
                 }

                else -> {
                    emit (ResponseStatus.Error())
                }
            }


    }
    override fun getTrackHistoryList(): List<TrackSearch> {
        val historyTracks = searchDataStorage.getSearchHistory().map {
            TrackSearch(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
        clickedHistoryTracks = historyTracks as ArrayList<TrackSearch>
        return historyTracks
    }
    override fun addTrackInHistory(track: TrackSearch) {
        clickedHistoryTracks.add(0,track)
        searchDataStorage.addTrackToHistory(
            TrackDto(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
        )
    }
    override fun clearHistory() {
        searchDataStorage.clearHistory()
    }

    companion object {

        var clickedHistoryTracks= arrayListOf<TrackSearch>()

    }

}