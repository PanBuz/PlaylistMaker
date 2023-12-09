package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.TrackSearch


interface MediaPlayerInteractor {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun currentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()

    fun getTrack() : TrackSearch

}