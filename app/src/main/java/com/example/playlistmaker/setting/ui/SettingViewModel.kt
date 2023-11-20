package com.example.playlistmaker.setting.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.App.Companion.respectMail
import com.example.playlistmaker.sharing.domain.App.Companion.messageToDevelopers
import com.example.playlistmaker.sharing.domain.App.Companion.respectText
import com.example.playlistmaker.sharing.domain.App.Companion.oferUrl
import com.example.playlistmaker.sharing.domain.App.Companion.shareTitle
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingViewModel (private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : ViewModel()
{
    private val themeMutableLiveData = MutableLiveData<Boolean>()
    val themeLiveData: LiveData<Boolean> = themeMutableLiveData

    init {
        Log.d("PAN_SettingViewModel", "onCreate")
        themeMutableLiveData.postValue(settingsInteractor.getThemeSettings())
    }
    override fun onCleared() {
        Log.d("PAN_SettingViewModel", "onCleared")
        super.onCleared()
    }
    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
        themeMutableLiveData.postValue(checked)
        Log.d("PAN_SettingViewModel", "switchTheme check")
    }
    fun getThemeState() : Boolean {
        return settingsInteractor.getThemeSettings()
        Log.d("PAN_SettingViewModel", "getThemeState")
    }
    fun shareApp() {
        Log.d("PAN_SettingViewModel", "shareApp")
        sharingInteractor.shareApp(App.shareText, shareTitle)
    }
    fun writeInSupport() {
        Log.d("PAN_SettingViewModel", "writeInSupport")
        sharingInteractor.openSupport(respectText, respectMail, messageToDevelopers)
    }
    fun openUserTerms() {
        Log.d("PAN_SettingViewModel", "openUserTerms")
        sharingInteractor.openTerms(oferUrl)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsInteractor = (this[APPLICATION_KEY] as App).provideSettingsInteractor()
                val sharingInteractor = (this[APPLICATION_KEY] as App).provideSharingInteractor()
                SettingViewModel(
                    sharingInteractor,
                    settingsInteractor
                )
            }
        }
    }

}