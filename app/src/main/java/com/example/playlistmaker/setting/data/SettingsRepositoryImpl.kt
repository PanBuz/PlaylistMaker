package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DARK_THEME_ENABLED
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsRepositoryImpl (private val preferences: SharedPreferences) : SettingsRepository {
    override fun getThemeSettings(): Boolean {
        return preferences.getBoolean(DARK_THEME_ENABLED, false)
    }
    override fun switchTheme(darkThemeEnabled: Boolean, ) {
        preferences.edit().putBoolean(DARK_THEME_ENABLED, darkThemeEnabled).apply() //записываем в файл настроек
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}