package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.mediateka.domain.favorite.FavoriteInteractorImpl
import com.example.playlistmaker.mediateka.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.mediateka.domain.newPlaylist.NewPlaylistInteractorImpl
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.data.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.data.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get(), get(), get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(get())
    }

}