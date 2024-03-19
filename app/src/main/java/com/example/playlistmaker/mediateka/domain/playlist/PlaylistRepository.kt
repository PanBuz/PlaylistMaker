package com.example.playlistmaker.mediateka.domain.playlist

import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun addNewTrack(track: TrackSearch, playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun deletePl (idPl : Int)
    suspend fun getPlaylistById (idPl : Int) : Playlist
    suspend fun deleteTrackFromPlaylist(trackId: String, idPl : Int  )
    suspend fun updatePl(idPl: Int?, namePl: String?,  descriptPl: String?)
    suspend fun deleteLinkTrackPl (trackId: String, idPl: Int)
    suspend fun deletePlfromTable (idPl : Int)
    suspend fun deleteLinkPl (idPl : Int)
    suspend fun deleteOrfanTrack ()
}