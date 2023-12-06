package com.example.playlistmaker.sharing.domain

interface SharingInteractor {

    fun shareApp(shareText:String, shareTitle: String)
    fun openSupport(respectText: String, respectMail: String, messageToDevelopers: String)
    fun openTerms(oferUrl:String)

}