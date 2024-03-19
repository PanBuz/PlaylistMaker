package com.example.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.mediateka.data.dao.FavoriteDao
import com.example.playlistmaker.mediateka.data.dao.LinkTrackPlDao
import com.example.playlistmaker.mediateka.data.dao.PlaylistDao
import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity
import com.example.playlistmaker.mediateka.data.entity.LinkTrackPlEntity
import com.example.playlistmaker.mediateka.data.entity.PlaylistEntity

@Database(version = 3, entities = [FavoriteTraksEntity::class, PlaylistEntity::class, LinkTrackPlEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteDao(): FavoriteDao
    abstract fun playlistDao() : PlaylistDao
    abstract fun linkTrackPlDao () : LinkTrackPlDao

}
