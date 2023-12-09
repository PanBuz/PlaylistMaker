package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl (private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.sendShare()
    }
    override fun openSupport() {
        externalNavigator.sendMail()
    }
    override fun openTerms() {
        externalNavigator.sendOfer()
    }

}