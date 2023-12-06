package com.example.playlistmaker.setting.data

import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsInteractorImpl (private val setRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return setRepository.getThemeSettings() !!
    }
    override fun switchTheme(darkThemeEnabled: Boolean) {
        setRepository.switchTheme(darkThemeEnabled)
    }
}