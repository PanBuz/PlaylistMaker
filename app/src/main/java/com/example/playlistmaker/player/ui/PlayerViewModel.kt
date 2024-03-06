package com.example.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackSearch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel  (private val mediaPlayerInteractor: MediaPlayerInteractor): ViewModel()
{

    private var timerJob: Job? = null
    private var favoriteJob: Job? = null
    private val RefreshDelayMs = 300L
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.DEFAULT())
    fun observePlayerState(): LiveData<PlayerState> = stateLiveData

    init {
        Log.d("PAN_MediaViewModel", "VM MediaViewModel onCreate")
        preparePlayer()
        setOnCompleteListener()
    }

    fun getTrack() : TrackSearch {
        return mediaPlayerInteractor.getTrack()
    }


    private fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(getTrack().previewUrl) {
            stateLiveData.postValue(PlayerState.PREPARED())
        }

    }
    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        stateLiveData.postValue(PlayerState.PLAYING(getCurrentPosition()))
        startTimer()
    }
    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        stateLiveData.postValue(PlayerState.PAUSED(getCurrentPosition()))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(RefreshDelayMs)
            while (stateLiveData.value is PlayerState.PLAYING) {
                delay(RefreshDelayMs)
                stateLiveData.postValue(PlayerState.PLAYING(getCurrentPosition()))
            }
        }
    }
    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayerInteractor.currentPosition()) ?: "00:00"
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            mediaPlayerInteractor.stopPlayer()
            preparePlayer()
        }
    }
    fun playbackControl() {
        when (stateLiveData.value) {
            is PlayerState.PLAYING -> {
                pausePlayer()
                Log.d("PAN_MediaViewModel", "pauseAudioPlayer()")
            }

            is PlayerState.PREPARED -> {
                startPlayer()
                Log.d("PAN_MediaViewModel", "startAudioPlayer()")
            }

            is PlayerState.PAUSED -> {
                Log.d("PAN_MediaViewModel", "prepareAudioPlayer()")
                startPlayer()
            }
            else -> {}
        }
    }


    fun likeOrDislike() {
        val playedTrack = getTrack()
        favoriteJob = viewModelScope.launch {
            if (playedTrack.isFavorite) {
                playedTrack.isFavorite = false
                deleteTrackFromFavorite(playedTrack.trackId)
            } else {
                playedTrack.isFavorite = true
                insertTrackToFavorite(playedTrack)
            }
        }
    }

    suspend fun insertTrackToFavorite(track: TrackSearch) {
        mediaPlayerInteractor.insertTrackToFavorite(track)
    }

    suspend fun deleteTrackFromFavorite(trackId: String) {
        mediaPlayerInteractor.deleteTrackFromFavorite(trackId)
    }

    override fun onCleared() {
        Log.d("PAN_MediaViewModel", "VM MediaViewModel onCleared")
        mediaPlayerInteractor.destroyPlayer()
    }

}