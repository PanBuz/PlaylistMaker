package com.example.playlistmaker.sharing.domain

interface ExternalNavigator  {
    fun sendShare ()
    fun sendMail ()
    fun sendOfer ()
    fun shareText(sharedText : String, sharedTitle : String)
}