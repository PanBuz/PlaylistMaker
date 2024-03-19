package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.convertor.TracksDbConvertor
import com.example.playlistmaker.mediateka.data.db.AppDatabase
import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity
import com.example.playlistmaker.mediateka.domain.favorite.FavoriteRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedHistoryTracks
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TracksDbConvertor,
) : FavoriteRepository {
    override fun favoriteTracks(): Flow<List<TrackSearch>> = flow {
        val tracks = appDatabase.favoriteDao().getFavoriteTracksByTime()
        emit(convertFromTrackEntity(tracks as List<FavoriteTraksEntity>))
    }
    override suspend fun deleteDbTrack(trackId: String) {
        appDatabase.favoriteDao().deleteTrack(trackId)
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTraksEntity>): List<TrackSearch> {
        return tracks.map { track -> trackDbConvertor.map(track) } as List<TrackSearch>
    }

    override fun setClickedTrack(track: TrackSearch) {
        clickedHistoryTracks.add(0, track)
    }

    private suspend fun saveTracks(tracks: List<TrackDto>) {
        val trackEntities = tracks.map { track -> trackDbConvertor.map(track) }
        appDatabase.favoriteDao().insertTraks(trackEntities)
    }

    private fun convertToTrackEntity(listTracks: List<TrackSearch>): List<FavoriteTraksEntity> {
        return listTracks.map { track -> trackDbConvertor.map(track) } as List<FavoriteTraksEntity>
    }

    override suspend fun insertDbTrackToFavorite(track: TrackSearch) {
        track.isFavorite = true
        val listTracks = arrayListOf<TrackSearch>()
        listTracks.add(track)
        val trackEntity = convertToTrackEntity(listTracks)
        appDatabase.favoriteDao().insertFavotiteTraks(trackEntity)
        clickedHistoryTracks.add(0, track)
    }

    override suspend fun deleteDbTrackFromFavorite(trackId: String) {
        val trackDislikeOnPosition =
            clickedHistoryTracks.filter { trackModel -> trackModel.trackId == trackId }[0]
        val position = clickedHistoryTracks.indexOf(trackDislikeOnPosition)
        clickedHistoryTracks[position].isFavorite = false
        appDatabase.favoriteDao().deleteTrackFromFavorite(trackId)
        appDatabase.linkTrackPlDao().deleteOrfanTrack()
    }
}