package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl (private val externalNavigator: ExternalNavigatorImpl) : SharingInteractor {

    override fun shareApp(shareText:String, shareTitle: String) {
        externalNavigator.sendShare(shareText,shareTitle )
    }
    override fun openSupport(respectText: String, respectMail: String, messageToDevelopers: String) {
        externalNavigator.sendMail(respectText, respectMail, messageToDevelopers)
    }
    override fun openTerms(oferUrl:String) {
        externalNavigator.sendOfer(oferUrl)
    }

}