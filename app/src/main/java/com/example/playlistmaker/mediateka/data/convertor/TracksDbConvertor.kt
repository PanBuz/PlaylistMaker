package com.example.playlistmaker.mediateka.data.convertor

import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.TrackSearch

class TracksDbConvertor {
    fun map(track: TrackDto): FavoriteTraksEntity {
        return FavoriteTraksEntity(
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
    }

    fun map(track: FavoriteTraksEntity): TrackSearch {
        return TrackSearch(
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
    }


    fun map(track: TrackSearch): FavoriteTraksEntity {
        return FavoriteTraksEntity(
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
    }
}