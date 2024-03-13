package com.example.playlistmaker.mediateka.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "playlist_table")
data class PlaylistEntity(

    @PrimaryKey (autoGenerate = true)
    val idPl: Int,
    val namePl: String,
    val descriptPl: String,
    val imagePl: String,
    val tracksPl: String,
    val countTracksPl: Int
)