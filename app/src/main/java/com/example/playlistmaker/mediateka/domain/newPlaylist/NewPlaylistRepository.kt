package com.example.playlistmaker.mediateka.domain.newPlaylist


import android.net.Uri

interface NewPlaylistRepository {
    suspend fun savePicture(uri: Uri, fileName: String)
    suspend fun loadPicture(fileName: String): Uri?
    fun imagePath () : String
}