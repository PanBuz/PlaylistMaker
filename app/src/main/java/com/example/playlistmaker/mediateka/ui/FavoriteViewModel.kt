package com.example.playlistmaker.mediateka.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FavoriteViewModel : ViewModel() {

    private var favorite_liveData = MutableLiveData<String>()
    val favoriteLiveData: LiveData<String> = favorite_liveData
}