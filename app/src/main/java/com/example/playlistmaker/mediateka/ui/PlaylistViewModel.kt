package com.example.playlistmaker.mediateka.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {

    private var playlist_liveData = MutableLiveData<String>()
    val playlistLiveData: LiveData<String> = playlist_liveData

}