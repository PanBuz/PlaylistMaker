package com.example.playlistmaker.mediateka.data.entity

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trackid_idpl_table")
data class LinkTrackPlEntity(

    @PrimaryKey(autoGenerate = true)
    val idLink: Int,
    val trackId: String,
    val idPl :  Int,
    val inPlTime : Long = Calendar.getInstance().time.time
)