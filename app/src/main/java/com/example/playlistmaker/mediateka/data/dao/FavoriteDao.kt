package com.example.playlistmaker.mediateka.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mediateka.data.entity.FavoriteTraksEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavotiteTraks(traks: List<FavoriteTraksEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraks(traks: List<FavoriteTraksEntity>)

    @Query("SELECT * FROM favoritetraks_table")
    suspend fun getTraks(): List<FavoriteTraksEntity>

    @Query("SELECT * FROM favoritetraks_table ORDER BY inDbTime DESC")
    suspend fun getTracksByTime(): List<FavoriteTraksEntity>

    @Query("SELECT * FROM favoritetraks_table WHERE trackId = :trackId")
    suspend fun getFavoriteTrack(trackId: String): List<FavoriteTraksEntity>

    @Query("DELETE FROM favoritetraks_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

}