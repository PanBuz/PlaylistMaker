package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.data.FavoriteRepositoryImpl
import com.example.playlistmaker.mediateka.data.NewPlaylistRepositoryImpl
import com.example.playlistmaker.mediateka.data.PlaylistRepositoryImpl
import com.example.playlistmaker.mediateka.data.convertor.TracksDbConvertor
import com.example.playlistmaker.mediateka.domain.favorite.FavoriteRepository
import com.example.playlistmaker.mediateka.domain.newPlaylist.NewPlaylistRepository
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistRepository
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.SharedPreferencesSearchHistoryStorage
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharedPrefsUtils
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single <SearchDataStorage> {
        SharedPrefsUtils(get(), get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get(), get())
    }

    single<SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    factory { TracksDbConvertor() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single <NewPlaylistRepository> {
        NewPlaylistRepositoryImpl (get())
    }

    single <PlaylistRepository> {
        PlaylistRepositoryImpl (get())
    }
}