package com.example.playlistmaker.search.domain

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackSearch(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    var artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
) {
    val coverUrl500 get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val durationTrack =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis)
}
