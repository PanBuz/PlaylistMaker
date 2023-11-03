package com.example.playlistmaker.domain

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String,  // Ссылка на трэк в iTunes
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String

)
{ val artworkUrl512 get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg") }