package com.example.playlistmaker.presentation.Ui

import android.annotation.SuppressLint
import android.app.Application
import android.media.MediaPlayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.App
import com.example.playlistmaker.domain.MUSIC_MAKER_PREFERENCES
import com.example.playlistmaker.domain.PlayerMedia
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SharedPrefsUtils
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale


class MediaActivity : AppCompatActivity() {

    companion object  {
        val STATE_DEFAULT = 0
        val STATE_PREPARED = 1
        val STATE_PLAYING = 2
        val STATE_PAUSED = 3
    }

    private var binding: ActivityMediaBinding? = null
    var mediaPlayer = MediaPlayer()
    private var playerState  = STATE_DEFAULT
    var  buttonPlay : MaterialButton? = null
    val handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)



        // Элементы экрана:
        val backOffImage = binding?.ivBack
        val cover = binding?.ivCover512
        val title = binding?.tvTitle
        val artist = binding?.tvArtist
        val buttonAdd = binding?.ivAdd
        buttonPlay = binding?.btPlay
        val buttonLike = binding?.ivLike
        val playback = binding?.tvPlaybackTime
        val durationTrack = binding?.tvDuration
        val album = binding?.tvAlbum
        val yearTrack = binding?.tvYear
        val genre = binding?.tvGenre
        val country = binding?.tvCountry

        backOffImage?.setOnClickListener { finish() }


        if (App.activeTracks.size > 0) {
            val playedTrack = App.activeTracks[0]

            val duration = SimpleDateFormat("mm:ss", Locale.getDefault() )
                .format(playedTrack.trackTimeMillis)
            title?.setText(playedTrack.trackName)
            artist?.setText(playedTrack.artistName)
            playback?.setText("0:00")
            durationTrack?.setText(duration)
            album?.setText(playedTrack.collectionName)
            yearTrack?.setText(playedTrack.releaseDate.substring(0, 4))
            genre?.setText(playedTrack.primaryGenreName)
            country?.setText(playedTrack.country)
            val coverUrl100 = playedTrack.artworkUrl100
            val coverUrl500 = playedTrack.artworkUrl512
            val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
            Glide.with(cover!!)
                .load(coverUrl500)
                .transform(RoundedCorners(radius))
                .placeholder(R.drawable.media_placeholder)
                .into(cover)
            val trackViewUrl = playedTrack.previewUrl
            buttonPlay?.isEnabled = false
            mediaPlayer.setDataSource(trackViewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                buttonPlay?.isEnabled = true
                playerState = PlayerMedia.STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {

                if (App.darkTheme) {buttonPlay?.setIconResource(R.drawable.button_play_night)}
                else {buttonPlay?.setIconResource(R.drawable.button_play_day)}
                playerState = PlayerMedia.STATE_PREPARED
            }
        }

        // воспроизведение музыки

        fun startPlayer() {
            mediaPlayer.start()
            if (App.darkTheme) {buttonPlay?.setIconResource(R.drawable.button_pause_night)}
            else {buttonPlay?.setIconResource(R.drawable.button_pause_day)}
            playerState = PlayerMedia.STATE_PLAYING
        }

        fun pausePlayer() {
            mediaPlayer.pause()
            if (App.darkTheme) {buttonPlay?.setIconResource(R.drawable.button_play_night)}
            else {buttonPlay?.setIconResource(R.drawable.button_play_day)}
            playerState = PlayerMedia.STATE_PAUSED
        }


        fun playbackControl() {
            when(playerState) {
                PlayerMedia.STATE_PLAYING -> {
                    pausePlayer()
                }
                PlayerMedia.STATE_PREPARED, PlayerMedia.STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }


        fun refreshTime() {
            val timeThread = Thread {
                handler.postDelayed(
                    object : Runnable {
                        override fun run() {
                            val trackPosition = SimpleDateFormat("mm:ss", Locale.getDefault() )
                                .format(mediaPlayer.currentPosition)
                            playback?.setText(trackPosition)

                            handler.postDelayed(this,333L)
                            if (playerState == PlayerMedia.STATE_PREPARED) {playback?.setText("00:00")}
                        }
                    },   333L
                )
            }.start()
        }


        buttonPlay?.setOnClickListener {
            playbackControl()
            if (playerState == PlayerMedia.STATE_PLAYING) {refreshTime()}
        }
    }

    override fun onDestroy(){
        super.onDestroy()

        mediaPlayer.pause()
        playerState = PlayerMedia.STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()

        mediaPlayer.pause()
        if (App.darkTheme) {buttonPlay?.setIconResource(R.drawable.button_play_night)}
        else {buttonPlay?.setIconResource(R.drawable.button_play_day)}
        playerState = PlayerMedia.STATE_PAUSED
    }
}

