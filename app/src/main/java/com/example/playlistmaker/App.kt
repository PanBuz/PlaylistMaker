package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.setting.data.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App as Application)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        shareText =  this.getText(R.string.course_url_string).toString()
        shareTitle =  this.getText(R.string.share_text).toString()
        respectText = this.getText(R.string.extra_text_string).toString()
        respectMail = this.getText(R.string.student_email).toString()
        messageToDevelopers = this.getText(R.string.extra_subject_string).toString()
        oferUrl = this.getText(R.string.oferta_url_string).toString()

        AppPreferences.setup(applicationContext)

        if (AppPreferences.darkTheme !=null) {
            darkTheme = AppPreferences.darkTheme !!
            switchTheme(darkTheme)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        var historyTracks= arrayListOf<TrackSearch>()
        var darkTheme = false
        var shareText = ""
        var shareTitle = ""
        var respectText = ""
        var respectMail = ""
        var messageToDevelopers = ""
        var oferUrl = ""
    }
}
