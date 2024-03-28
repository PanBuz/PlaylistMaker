package com.example.playlistmaker.mediateka.data

import android.util.Log
import com.example.playlistmaker.mediateka.data.db.AppDatabase
import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity
import com.example.playlistmaker.mediateka.data.entity.LinkTrackPlEntity
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
        appDatabase.playlistDao().updatePlaylist(convertToEntityPlaylist(playlist))
        appDatabase.linkTrackPlDao().insertLinkPl(LinkTrackPlEntity(0, track.trackId, playlist.id))
        appDatabase.favoriteDao().insertTrack(convertToTrackDto(track))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = appDatabase.playlistDao().getPlaylists()
        return playlistFlow.map { playlist -> playlist.map { convertToPlaylist(it) } }
    }

    override suspend fun deletePl (idPl : Int)  {
        appDatabase.playlistDao().deletePl (idPl)
        deletePlfromTable(idPl)
        deleteLinkPl(idPl)
    }

    override  suspend fun deletePlfromTable (idPl : Int){
        Log.d("PAN_PlRepoImpl", "Удаляем плэйдист из таблицы плэйлистов ($idPl)")
        appDatabase.playlistDao().deletePl (idPl)
    }
    override suspend fun deleteLinkPl (idPl : Int){
        Log.d("PAN_PlRepoImpl", "Удаляем связи для несуществующих плэйлистов")
        appDatabase.linkTrackPlDao().deleteLinkPl()
    }

    override suspend fun deleteOrfanTrack () {
        Log.d("PAN_PlRepoImpl", "Удаляем трэки, которых нет ни в одном плэйлисте")
        appDatabase.linkTrackPlDao().deleteOrfanTrack()
    }

    override suspend fun deleteLinkTrackPl (trackId: String, idPl: Int) {
        Log.d("PAN_PlRepoImpl", "Удаляем связи трэка с плэйлистом в trackid_idpl_table при удалении трэка из плэйлиста")
        appDatabase.linkTrackPlDao().deleteLinkTrackPl(trackId, idPl)
    }

    override suspend fun getPlaylistById (idPl : Int) : Playlist {
        val playlist = appDatabase.playlistDao().getPlaylistById (idPl)
        return convertToPlaylist(playlist)
    }

    override suspend fun updatePl(idPl: Int?, namePl: String?, descriptPl: String?) {
        Log.d ("PlaylistRepositoryImpl", "updatePl idPl= ${idPl} ")
        appDatabase.playlistDao().updatePl(idPl, namePl,  descriptPl)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, idPl: Int) {
        val playlist = convertToPlaylist(appDatabase.playlistDao().getPlaylistById(idPl))
        for (track in playlist.tracks) {
            if (track.trackId == trackId) {
                playlist.tracks.remove(track)
                Log.d ("PlaylistRepositoryImpl", "4. Удалил track с trackId= ${track.trackId} ")
                break
            }
        }
        appDatabase.playlistDao().updatePlaylist(convertToEntityPlaylist(playlist))

    }

    private fun convertToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        var timeAllTracks = 0L
        for (track in playlist.tracks) {
            timeAllTracks += track.trackTimeMillis
        }
        return PlaylistEntity(
            idPl = playlist.id,
            namePl = playlist.name,
            descriptPl = playlist.descript,
            tracksPl = convertListToString(playlist.tracks),
            countTracksPl = playlist.tracks.size,
            timePl = timeAllTracks
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
            tracks = convertStringToList(playlist.tracksPl),
            countTracks = playlist.countTracksPl,
            timePl=playlist.timePl
        )
    }
    private fun convertToTrackDto(track: TrackSearch) =
        FavoriteTraksEntity(
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
}