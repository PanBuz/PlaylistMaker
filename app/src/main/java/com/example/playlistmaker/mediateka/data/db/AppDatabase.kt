package com.example.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateka.data.db.dao.FavoriteDao
import com.example.playlistmaker.mediateka.data.db.entity.FavoriteTraksEntity

@Database(version = 1, entities = [FavoriteTraksEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun likeDao(): FavoriteDao
}