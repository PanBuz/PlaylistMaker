package com.example.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.mediateka.data.dao.FavoriteDao
import com.example.playlistmaker.mediateka.data.dao.PlaylistDao
import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity
import com.example.playlistmaker.mediateka.data.entity.PlaylistEntity

@Database(version = 3, entities = [FavoriteTraksEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun likeDao(): FavoriteDao
    abstract fun playlistDao() : PlaylistDao

    /*val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Ваш код миграции здесь
            // Например, если вы хотите добавить новую таблицу, выполните следующее:
            database.execSQL("CREATE TABLE IF NOT EXISTS new_table (id INTEGER PRIMARY KEY, name TEXT NOT NULL)")

            // Или если вы хотите изменить существующую таблицу, например, добавить новый столбец:
            database.execSQL("ALTER TABLE existing_table ADD COLUMN new_column TEXT")
        }
    }*/
}
