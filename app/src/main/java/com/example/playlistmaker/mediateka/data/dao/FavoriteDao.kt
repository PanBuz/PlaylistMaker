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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTraksEntity)

    @Query("SELECT * FROM favoritetraks_table  WHERE isFavorite=1 ORDER BY inDbTime DESC")
    suspend fun getFavoriteTracksByTime(): List<FavoriteTraksEntity>

    @Query("SELECT * FROM favoritetraks_table WHERE isFavorite=1 AND trackId = :trackId")
    suspend fun getFavoriteTrack (trackId : String) : List<FavoriteTraksEntity>

    @Query("UPDATE favoritetraks_table SET isFavorite=0 WHERE trackId = :trackId")
    suspend fun deleteTrackFromFavorite (trackId: String)

    @Query("DELETE FROM favoritetraks_table WHERE isFavorite=0 AND trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("DELETE FROM favoritetraks_table WHERE isFavorite=1 AND trackId = :trackId"  )
    suspend fun deleteFavoriteTrack(trackId: String)

}