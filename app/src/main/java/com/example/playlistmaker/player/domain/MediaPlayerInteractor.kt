package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.TrackSearch


interface MediaPlayerInteractor {

    val isPlaying : Boolean

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun currentPosition(): Int

    fun startPlayer()

    fun pausePlayer()
    fun stopPlayer()

    fun destroyPlayer()

    fun getTrack() : TrackSearch

    suspend fun saveTrack(track: TrackSearch)

    fun isNightTheme(): Boolean

    suspend fun insertTrackToFavorite(track: TrackSearch)

    suspend fun deleteTrackFromFavorite(trackId: String)

}