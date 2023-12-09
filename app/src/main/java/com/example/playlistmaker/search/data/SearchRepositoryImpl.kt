package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.search.domain.ResponseStatus
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackSearch
import java.lang.Error
import javax.net.ssl.HttpsURLConnection

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchDataStorage: SearchDataStorage
) : SearchRepository {


    override fun searchTrack(expression: String): ResponseStatus<List<TrackSearch>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        Log.d ("PAN_SearchRepositoryImpl", "Пришло в searchTrack_SearchRepository (${expression})")
        try {
            return when (response.resultCode) {
                -1 -> {
                    ResponseStatus.Error()
                }

                HttpsURLConnection.HTTP_OK -> {
                    Log.d ("PAN_SearchRepositoryImpl", "resultCode in searchTrack_SearchRepository (${response.resultCode})")
                    ResponseStatus.Success((response as TracksSearchResponse).results.map {
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
                    })
                }

                else -> {
                    ResponseStatus.Error()
                }
            }
        } catch (error : Error) {
            Log.d ("PAN_SearchRepositoryImpl", "Ошибка в searchTrack_SearchRepository (${error})")
            return ResponseStatus.Error()
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