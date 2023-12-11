package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.google.gson.Gson

class SharedPreferencesSearchHistoryStorage(
    private val prefs: SharedPreferences,
    private val gson: Gson
) : SearchHistoryStorage {

}