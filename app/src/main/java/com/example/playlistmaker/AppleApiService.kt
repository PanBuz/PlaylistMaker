package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleApiService {
    @GET("search?entity=song")
    fun search(@Query("term", encoded = false) text: String): Call<TracksResponse>
}

class ITunesSearch(searchedText: String, val onSearchListener : OnSearchListener)
{
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(AppleApiService::class.java)
    var tracksITunes :ArrayList <Track> = arrayListOf()



    class OnSearchListener(val searchListener: (position: Int) -> Unit) {
        fun onSearch(position: Int) = searchListener(position)
    }
}