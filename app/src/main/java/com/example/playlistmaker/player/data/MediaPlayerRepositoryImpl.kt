package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedHistoryTracks

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : MediaPlayerRepository {

    override val isPlaying = mediaPlayer.isPlaying

    override fun preparePlayer(url: String, preparedListener: () -> Unit) {
        val sourse = clickedHistoryTracks[0].previewUrl
        Log.d("PAN_MediaPlayerRepositoryImpl_preparePlayer", sourse)
        mediaPlayer.setDataSource(sourse)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            preparedListener()
        }
    }
    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onCompletionListener() }
    }
    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }
    override fun startPlayer() {
        mediaPlayer.start()
    }
    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun stopPlayer() {
        mediaPlayer.reset()
    }
    override fun destroyPlayer() {
        mediaPlayer.release()
    }
    override fun getTrack() : TrackSearch {
        return clickedHistoryTracks[0]
    }

    override fun isNightTheme(): Boolean {
        return App.darkTheme
    }


}