package com.example.playlistmaker.mediateka.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel (private val interactor : PlaylistInteractor)  : ViewModel() {

    private var playlist_liveData = MutableLiveData<PlaylistState>()
    val playlistLiveData: LiveData<PlaylistState> = playlist_liveData

    fun getPlaylist() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { processResult(it) }
        }
    }
    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            playlist_liveData.postValue(PlaylistState.Empty)
        } else {
            playlist_liveData.postValue(PlaylistState.Playlists(playlists))
        }
    }
}