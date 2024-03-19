package com.example.playlistmaker.mediateka.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.mediateka.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist_table WHERE idPl = :idPl")
    suspend fun getPl(idPl : Int): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPl(playlist: PlaylistEntity)

    @Update( onConflict = OnConflictStrategy.REPLACE, entity = PlaylistEntity::class)
    suspend fun updatePl(playlist: PlaylistEntity)

}