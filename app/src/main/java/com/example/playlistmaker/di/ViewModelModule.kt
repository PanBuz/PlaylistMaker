package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.setting.ui.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.mediateka.ui.FavoriteViewModel
import com.example.playlistmaker.mediateka.ui.PlaylistViewModel

val viewModelModule = module {


    viewModel {
        SearchViewModel(
            get()
        )
    }

    viewModel {
        PlayerViewModel(
            get<MediaPlayerInteractor>()
        )
    }

    viewModel <SettingViewModel> {
        SettingViewModel(
            get(),
            get()
        )
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }

}