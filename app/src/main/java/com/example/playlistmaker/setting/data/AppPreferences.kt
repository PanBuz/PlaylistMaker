package com.example.playlistmaker.setting.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.MUSIC_MAKER_PREFERENCES
object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null
    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences(MUSIC_MAKER_PREFERENCES, MODE_PRIVATE)
    }
    var darkTheme: Boolean?
        get() = Key.DARK_THEME_ENABLED.getBoolean()
        set(value) = Key.DARK_THEME_ENABLED.setBoolean(value)

    private enum class Key {
        DARK_THEME_ENABLED;
        fun getBoolean(): Boolean? = if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(name, false) else null
        fun setBoolean(value: Boolean?) = value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()
        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}