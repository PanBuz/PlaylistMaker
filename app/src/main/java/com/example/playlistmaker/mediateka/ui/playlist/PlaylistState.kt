package com.example.playlistmaker.mediateka.ui.playlist

import com.example.playlistmaker.mediateka.domain.Playlist

sealed class PlaylistState {
    class Playlists(val playlists: List<Playlist>) : PlaylistState()
    object Empty : PlaylistState()
}