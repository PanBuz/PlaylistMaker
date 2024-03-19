package com.example.playlistmaker.mediateka.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "favoritetraks_table")
data class FavoriteTraksEntity(
    @PrimaryKey
    val trackId:  String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite : Boolean = false,
    val inDbTime : Long = Calendar.getInstance().time.time
)
