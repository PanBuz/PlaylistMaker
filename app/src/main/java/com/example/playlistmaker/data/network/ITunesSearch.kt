package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesSearch(searchedText: String, val onSearchListener : OnSearchListener)
{
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    var tracksITunes :ArrayList <Track> = arrayListOf()



    class OnSearchListener(val searchListener: (position: Int) -> Unit) {
        fun onSearch(position: Int) = searchListener(position)
    }
}