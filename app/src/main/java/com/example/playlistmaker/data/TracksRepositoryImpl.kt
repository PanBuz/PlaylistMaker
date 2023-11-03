package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.domain.api.TracksRepository
import java.net.HttpURLConnection

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == HttpURLConnection.HTTP_OK) {
            return (response as ITunesResponse).results.map {
                Track(
                    it.trackId.toInt(),
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
        } else {
            return emptyList()
        }
    }
}