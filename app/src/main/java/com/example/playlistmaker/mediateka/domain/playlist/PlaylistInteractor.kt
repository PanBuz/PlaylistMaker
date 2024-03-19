package com.example.playlistmaker.mediateka.domain.playlist

import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun addNewTrack(track: TrackSearch, playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}