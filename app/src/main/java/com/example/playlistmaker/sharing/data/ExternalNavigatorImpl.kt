package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.sharing.domain.ExternalNavigator


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {
    override fun sendShare(shareText:String, shareTitle: String)  {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        executeIntent(Intent.createChooser(sendIntent, shareTitle))
    }
    override fun sendMail(respectText: String, respectMail: String, messageToDevelopers: String)  {
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(respectMail))
        mailIntent.putExtra(Intent.EXTRA_TEXT, respectText)
        mailIntent.putExtra(Intent.EXTRA_SUBJECT,messageToDevelopers)
        executeIntent(mailIntent)
    }
    override fun sendOfer(oferUrl:String) {
        val oferIntent = Intent(Intent.ACTION_VIEW, Uri.parse(oferUrl as String?))
        executeIntent(oferIntent)
    }
    fun executeIntent (intent: Intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(intent)
        }
        catch (exception: Exception) {
            Log.e ("PAN_executeIntent_exception", exception.toString())
        }
    }
}