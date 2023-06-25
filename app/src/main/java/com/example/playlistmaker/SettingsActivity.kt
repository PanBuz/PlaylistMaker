package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.net.Uri

class SettingsActivity : AppCompatActivity() {

    private val studentEmail = "aleksandor1203@yandex.ru"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.backSettings)
        val shareButton = findViewById<Button>(R.id.shareBtm)
        val supportButton = findViewById<Button>(R.id.supportBtm)
        val licenceBottom: Button = findViewById(R.id.licenceBtm)

        backButton.setOnClickListener {
            finish()
        }

        // Обработчик нажатия на кнопку "Поделиться приложением"
        shareButton.setOnClickListener {
            shareCourse()
        }

        supportButton.setOnClickListener {
            sendEmailToSupport()
        }

        licenceBottom.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val url_oferta = getResources().getString(R.string.oferta_url_string)
            intent.data = Uri.parse(url_oferta)
            startActivity(intent)
        }

    }

    private fun shareCourse() {

        Intent().apply {
            action = Intent.ACTION_SEND
            val courseUrl = getResources().getString(R.string.course_url_string)
            putExtra(Intent.EXTRA_TEXT, courseUrl)
            type = "text/plain"
            startActivity(Intent.createChooser(this, "Поделиться приложением"))
        }
    }

    private fun sendEmailToSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(studentEmail))
            val extraSubjectString = getResources().getString(R.string.course_url_string)
            val extraTextString = getResources().getString(R.string.course_url_string)
            putExtra(
                Intent.EXTRA_SUBJECT,
                extraSubjectString
            )
            putExtra(
                Intent.EXTRA_TEXT,
                extraTextString
            )
        }

        startActivity(intent)
    }

}