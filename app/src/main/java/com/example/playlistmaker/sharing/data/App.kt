package com.example.playlistmaker.sharing.data

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.setting.data.AppPreferences
import com.example.playlistmaker.setting.data.SettingsInteractorImpl
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

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

    fun getSettingsRepository(): SettingsRepositoryImpl {
        return SettingsRepositoryImpl()
    }

    fun getExternalNavigator(): ExternalNavigatorImpl {
        return ExternalNavigatorImpl(this)
    }
    /*fun getMediaPlayerRepository(): MediaPlayerRepositoryImpl {
        return MediaPlayerRepositoryImpl()
    }*/

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
    /*fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(getMediaPlayerRepository())
    }*/

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
