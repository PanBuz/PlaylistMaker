package com.example.playlistmaker.search.domain

data class TrackSearch(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    val coverUrl500 get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
