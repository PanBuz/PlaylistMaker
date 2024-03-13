package com.example.playlistmaker.player.ui

import com.example.playlistmaker.mediateka.domain.Playlist

sealed class ReplyOnAddTrack {
    class Contained(val playlist: Playlist) : ReplyOnAddTrack()
    class Added(val playlist: Playlist) : ReplyOnAddTrack()
}