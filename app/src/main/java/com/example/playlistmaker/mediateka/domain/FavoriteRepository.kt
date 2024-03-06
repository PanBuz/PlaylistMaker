package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun favoriteTracks(): Flow<ArrayList<TrackSearch>>

    suspend fun deleteDbTrack (trackId :String)

    fun setClickedTrack(track: TrackSearch)

    suspend fun insertDbTrackToFavorite (track: TrackSearch)

    suspend fun deleteDbTrackFromFavorite(trackId: String)
}