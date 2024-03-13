package com.example.playlistmaker.mediateka.domain.playlist

import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }
    override suspend fun addNewTrack(track: TrackSearch, playlist: Playlist) {
        repository.addNewTrack(track, playlist)
    }
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

}