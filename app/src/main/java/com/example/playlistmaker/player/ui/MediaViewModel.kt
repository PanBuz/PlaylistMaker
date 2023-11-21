package com.example.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackSearch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel   (application: Application): AndroidViewModel(application)
{

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<PlayerState>()
    private val timeLiveData = MutableLiveData<String>()
    private var clickAllowed = true
    private val RefreshDelayMs = 500L
    private val ClickDelayMs = 100L

    fun observScreen(): LiveData<PlayerState> = stateLiveData
    fun observTimer(): LiveData<String> = timeLiveData

    init {
        Log.d("PAN_MediaViewModel", "VM MediaViewModel onCreate")
        updateState(PlayerState.DEFAULT)
        preparePlayer()
        setOnCompleteListener()
        isClickAllowed()
    }
    fun getTrack() : TrackSearch {
        return mediaPlayerInteractor.getTrack()
    }

    private fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(getTrack().previewUrl) {
            updateState(PlayerState.PREPARE)
        }

    }
    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        updateState(PlayerState.PLAYING(mediaPlayerInteractor.currentPosition()))
    }
    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        updateState(PlayerState.PAUSED)
    }
    private fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.currentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            updateState(PlayerState.PREPARE)
        }
    }
    fun playbackControl() {
        when (stateLiveData.value) {
            is PlayerState.PLAYING -> {
                pausePlayer()
                Log.d("PAN_MediaViewModel", "pauseAudioPlayer()")
            }

            is PlayerState.PREPARE, PlayerState.PAUSED -> {
                startPlayer()
                handler.post(updateTime())
                Log.d("PAN_MediaViewModel", "startAudioPlayer()")
            }

            else -> {preparePlayer()
                Log.d("PAN_MediaViewModel", "prepareAudioPlayer()")
            }
        }
    }

    private fun updateState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
    override fun onCleared() {
        Log.d("PAN_MediaViewModel", "VM MediaViewModel onCleared")
        handler.removeCallbacksAndMessages(null)
        mediaPlayerInteractor.destroyPlayer()
    }
    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                timeLiveData.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(getCurrentPosition())
                )
                handler.postDelayed(this, RefreshDelayMs)
            }
        }
    }
    fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, ClickDelayMs)
        }
        return current
    }


}