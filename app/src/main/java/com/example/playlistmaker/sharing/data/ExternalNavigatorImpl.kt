package com.example.playlistmaker.sharing.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {

    val shareText =  application.getText(R.string.course_url_string).toString()
    val shareTitle =  application.getText(R.string.share_text).toString()
    val respectText = application.getText(R.string.extra_text_string).toString()
    val respectMail = application.getText(R.string.student_email).toString()
    val messageToDevelopers = application.getText(R.string.extra_subject_string).toString()
    val oferUrl = application.getText(R.string.oferta_url_string).toString()
    override fun sendShare()  {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        executeIntent(Intent.createChooser(sendIntent, shareTitle))
    }
    override fun sendMail()  {
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(respectMail))
        mailIntent.putExtra(Intent.EXTRA_TEXT, respectText)
        mailIntent.putExtra(Intent.EXTRA_SUBJECT,messageToDevelopers)
        executeIntent(mailIntent)
    }
    override fun sendOfer() {
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
    override fun shareText(sharedText : String, sharedTitle : String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sharedText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, sharedTitle)
        executeIntent(shareIntent)
    }
}