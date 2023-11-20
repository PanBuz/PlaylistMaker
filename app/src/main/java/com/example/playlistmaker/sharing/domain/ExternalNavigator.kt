package com.example.playlistmaker.sharing.domain

interface ExternalNavigator  {

    fun sendShare (shareText:String, shareTitle: String)

    fun sendMail (respectText: String, respectMail: String, messageToDevelopers: String)

    fun sendOfer (oferUrl:String)
}