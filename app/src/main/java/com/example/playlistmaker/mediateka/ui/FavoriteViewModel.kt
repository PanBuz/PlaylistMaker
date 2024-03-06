package com.example.playlistmaker.mediateka.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.domain.FavoriteInteractor
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.launch


class FavoriteViewModel(
    private val context: Context,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private var favorite_liveData = MutableLiveData<FavoriteState>()
    val favoriteLiveData: LiveData<FavoriteState> = favorite_liveData

    init {
        fillData()
    }

    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            favoriteInteractor
                .favoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: ArrayList<TrackSearch>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty(context.getString(R.string.empty_library)))
        } else {
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        favorite_liveData.postValue(state)
    }

    fun setClickedTrack(track: TrackSearch, favoriteFragment: FavoriteFragment) {
        favoriteInteractor.setClickedTrack(track)
    }
}


