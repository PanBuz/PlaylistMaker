package com.example.playlistmaker.mediateka.ui.displayPlaylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.mediateka.ui.playlist.PlaylistState
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

class DisplayPlaylistViewModel(private val interactor : PlaylistInteractor,
                               private val newPlaylistInteractor: NewPlaylistInteractor,
                               private val searchInteractor : SearchInteractor,
                               private val sharingInteractor : SharingInteractor) : ViewModel(){

    private var _playlistLiveData = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = _playlistLiveData
    private var _liveData = MutableLiveData<PlaylistState>()
    val liveData: LiveData<PlaylistState> = _liveData

    // Получить нужный плэйлист и обработать его
    fun getPlaylistById(idPl: Int) {
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(idPl)
            _playlistLiveData.postValue(playlist)
        }
    }
    fun getPlaylist() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { processResult(it) }
        }
    }
    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _liveData.postValue(PlaylistState.Empty)
        } else {
            _liveData.postValue(PlaylistState.Playlists(playlists))
        }
    }

    fun addTrackToHistory(track:TrackSearch) {
        viewModelScope.launch { searchInteractor.addTrackToHistory(track) }
    }
    fun deleteTrackFromPlaylist(trackId: String, idPl : Int  ) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(trackId, idPl) }
        viewModelScope.launch {
            interactor.deleteLinkTrackPl(trackId, idPl)
            getPlaylistById(idPl)}
        viewModelScope.launch {
            interactor.deleteOrfanTrack()}
    }
    fun deletePl (idPl : Int){
        viewModelScope.launch {
            interactor.deletePlfromTable(idPl)
        }
        viewModelScope.launch {
            interactor.deleteLinkPl(idPl)
        }
        viewModelScope.launch {
            interactor.deleteOrfanTrack()}
    }
    fun imagePath () : String {
        return newPlaylistInteractor.imagePath()
    }
    fun sharePlaylist(sharedPlaylist: String, titlePlaylist:String) {
        sharingInteractor.shareText(sharedPlaylist, titlePlaylist)
    }
}