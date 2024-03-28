package com.example.playlistmaker.mediateka.domain.newPlaylist

import android.net.Uri

class NewPlaylistInteractorImpl(private val repository: NewPlaylistRepository) :
    NewPlaylistInteractor {
    override suspend fun savePicture(uri: Uri, fileName: String) {
        repository.savePicture(uri, fileName)
    }
    override suspend fun loadPicture(fileName: String): Uri? {
        return repository.loadPicture(fileName)
    }
    override fun imagePath () : String {
        return repository.imagePath()
    }
    override suspend fun deletePicture(oldNamePl: String) {
        return repository.deletePicture(oldNamePl)
    }
    override suspend fun renameFile(oldName: String, newName: String) : Boolean {
        return repository.renameFile(oldName, newName)
    }
}