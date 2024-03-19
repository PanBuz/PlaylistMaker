package com.example.playlistmaker.mediateka.ui.updatePlaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.mediateka.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.mediateka.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.mediateka.ui.newPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class UpdatePlaylistViewModel(
    interactor: PlaylistInteractor,
    newPlaylistInteractor: NewPlaylistInteractor
) : NewPlaylistViewModel(interactor, newPlaylistInteractor) {
    private var _updateLiveData = MutableLiveData<Playlist>()
    val updateLiveData: LiveData<Playlist> = _updateLiveData
    private val _update = MutableLiveData<Boolean>()
    val update: LiveData<Boolean> = _update


    fun updatePl(idPl: Int?, namePl: String?, descriptPl: String?) {
        viewModelScope.launch {
            interactor.updatePl(idPl, namePl,  descriptPl)
        }
    }


    fun deletePicture(oldNamePl: String) {
        Log.d("PAN_NewPlaylistVM", "Готовим на удаление  ${oldNamePl}")
        viewModelScope.launch {
            newPlaylistInteractor.deletePicture(oldNamePl)
            Log.d("PAN_NewPlaylistVM", "Удалили = ${oldNamePl}")
        }
    }

    override fun savePicture(uri: Uri?, namePl: String) {
        Log.d("PAN_NewPlaylistVM", "Готовим на сохранение = ${uri?.encodedPath}")
        viewModelScope.launch {
            if (uri != null) {
                newPlaylistInteractor.savePicture(uri, namePl)
                Log.d("PAN_NewPlaylistVM", "Отправляем на сохранение = ${uri.encodedPath}")
            }
        }
    }

    fun initialization() {
        // Второй вариант передачи данных плэйлиста из DisplayPlaylistFragment
        val actualPlaylist = DisplayPlaylistFragment.actualPlaylist
        _updateLiveData.postValue(actualPlaylist !!)
    }
}