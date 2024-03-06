package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.MUSIC_MAKER_PREFERENCES
import com.example.playlistmaker.mediateka.data.db.AppDatabase
import com.example.playlistmaker.search.data.SharedPreferencesSearchHistoryStorage
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    factory <MediaPlayer>{
        MediaPlayer()
    }

    factory { Gson() }

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(MUSIC_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

}