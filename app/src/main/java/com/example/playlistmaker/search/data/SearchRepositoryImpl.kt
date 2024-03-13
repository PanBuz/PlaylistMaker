package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.mediateka.data.db.AppDatabase
import com.example.playlistmaker.mediateka.data.convertor.TracksDbConvertor
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
    private val searchDataStorage: SearchDataStorage,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TracksDbConvertor
) : SearchRepository {


    override suspend fun searchTrack(searchQuery: String): Flow<ResponseStatus<List<TrackSearch>>> =
        flow {
            val response = networkClient.doRequest(TracksSearchRequest(searchQuery))
            Log.d(
                "PAN_SearchRepositoryImpl",
                "Пришло в searchTrack_SearchRepository (${searchQuery})"
            )

            when (response.resultCode) {
                -1 -> {
                    emit(ResponseStatus.Error())
                }

                200 -> {
                    with(response as TracksSearchResponse) {
                        val data = results.map {
                            TrackSearch(
                                trackId = it.trackId,
                                trackName = it.trackName,
                                artistName = it.artistName,
                                trackTimeMillis = it.trackTimeMillis,
                                artworkUrl100 = it.artworkUrl100,
                                collectionName = it.collectionName,
                                releaseDate = it.releaseDate,
                                primaryGenreName = it.primaryGenreName,
                                country = it.country,
                                previewUrl = it.previewUrl,
                                isFavorite = checkIsFavorite(it.trackId)
                            )
                        }
                        emit(ResponseStatus.Success(data))
                    }
                }

                else -> {
                    emit(ResponseStatus.Error())
                }
            }


        }

    suspend fun checkIsFavorite(trackId: String): Boolean {
        return (appDatabase.likeDao().getFavoriteTrack(trackId).size > 0)
    }

    override suspend fun getTrackHistoryList(): List<TrackSearch> {
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
                it.previewUrl,
                it.isFavorite
            )
        }
        clickedHistoryTracks.clear()
        clickedHistoryTracks.addAll(historyTracks)
        return clickedHistoryTracks
    }

    override suspend fun addTrackToHistory(track: TrackSearch) {

        for (clickedTrack in clickedHistoryTracks) {
            if (clickedTrack.trackId == track.trackId) {
                clickedHistoryTracks.remove(clickedTrack)
                Log.d("Pan_search_repository", "4. Удалил запись с trackId= ${track.trackId} ")  //1
                break
            }
        }

        clickedHistoryTracks.add(0, track)
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
                track.previewUrl,
                track.isFavorite
            )
        )
    }

    override suspend fun clearHistory() {
        clickedHistoryTracks.clear()
        searchDataStorage.clearHistory()
    }

    companion object {

        var clickedHistoryTracks = arrayListOf<TrackSearch>()

    }

}