package com.example.playlistmaker.mediateka.domain.favorite

import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) :
    FavoriteInteractor {
    override fun favoriteTracks(): Flow<List<TrackSearch>> {

        return favoriteRepository.favoriteTracks()
    }
    override fun setClickedTrack(track: TrackSearch) {
        favoriteRepository.setClickedTrack(track)
    }
}