package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.AppDatabase
import com.example.playlistmaker.mediateka.data.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.domain.TrackSearch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type

class PlaylistRepositoryImpl (val appDatabase: AppDatabase) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPl(convertToEntityPlaylist(playlist))
    }

    override suspend fun addNewTrack(track: TrackSearch, playlist: Playlist) {
        playlist.tracks.add(0, track)
        playlist.countTracks = playlist.tracks.size
        appDatabase.playlistDao().updatePl(convertToEntityPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = appDatabase.playlistDao().getPlaylists()
        return playlistFlow.map { playlist -> playlist.map { convertToPlaylist(it) } }
    }

    private fun convertToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            idPl = playlist.id,
            namePl = playlist.name,
            descriptPl = playlist.descript,
            imagePl = playlist.image,
            tracksPl = convertListToString(playlist.tracks),
            countTracksPl = playlist.countTracks
        )
    }

    private fun convertListToString(tracksPl: ArrayList<TrackSearch>): String {
        return  Gson().toJson(tracksPl)
    }

    private fun convertStringToList(tracksId: String): ArrayList<TrackSearch> {
        val type: Type = object : TypeToken<ArrayList<TrackSearch?>?>() {}.type
        return Gson().fromJson(tracksId, type) as ArrayList<TrackSearch>
    }

    private fun convertToPlaylist(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.idPl,
            name = playlist.namePl,
            descript = playlist.descriptPl,
            image = playlist.imagePl,
            tracks = convertStringToList(playlist.tracksPl),
            countTracks = playlist.countTracksPl
        )
    }
}