package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedHistoryTracks

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : MediaPlayerRepository {

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
    override fun destroyPlayer() {
        mediaPlayer.release()
    }
    override fun getTrack() : TrackSearch {

        /*if (clickedHistoryTracks.isNullOrEmpty()) {
            val defaultTracks =  arrayListOf<TrackSearch>((TrackSearch(1,"Сначала нужно выбрать песню", "Александр Панкратов",100,  "", "", "2023", "", "RU", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/76/8b/a5/768ba505-7774-9094-f3b7-8f55479df229/mzaf_5299098203351413278.plus.aac.p.m4a")))
            clickedHistoryTracks.add(defaultTracks[0])
        }*/ // на экране медиатеки почему - то запускается экран плейера, должен быть пустой экран. (не совсем понял, что подразумевается под пустым экраном?, сделал эту логику чтобы приложение не вылетало при первой попытке открыть медиатеку)

        return clickedHistoryTracks[0]
    }

}