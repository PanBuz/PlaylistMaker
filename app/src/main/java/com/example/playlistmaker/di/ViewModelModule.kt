package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.mediateka.ui.displayPlaylist.DisplayPlaylistViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.setting.ui.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.mediateka.ui.favorite.FavoriteViewModel
import com.example.playlistmaker.mediateka.ui.newPlaylist.NewPlaylistViewModel
import com.example.playlistmaker.mediateka.ui.playlist.PlaylistViewModel
import com.example.playlistmaker.mediateka.ui.updatePlaylist.UpdatePlaylistViewModel
import org.koin.android.ext.koin.androidContext

val viewModelModule = module {

    viewModel {
        SearchViewModel(
            get()
        )
    }

    viewModel {
        PlayerViewModel(
            get<MediaPlayerInteractor>(),
            get<PlaylistInteractor>()
        )
    }

    viewModel <SettingViewModel> {
        SettingViewModel(
            get(),
            get()
        )
    }

    viewModel {
        FavoriteViewModel(androidContext(), get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        DisplayPlaylistViewModel(get(), get(), get(), get())
    }

    viewModel {
        UpdatePlaylistViewModel(get(), get())
    }

}