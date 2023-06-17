package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.net.Uri

class SettingsActivity : AppCompatActivity() {

    private val courseUrl = "https://yandex.ru/praktikum"
    private val studentEmail = "aleksandor1203@yandex.ru"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.backSettings)
        val shareButton = findViewById<Button>(R.id.shareBtm)
        val supportButton = findViewById<Button>(R.id.supportBtm)
        val licenceBottom: Button = findViewById(R.id.licenceBtm)

        backButton.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
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
            val url = "https://praktikum.yandex.ru/oferta/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }

    private fun shareCourse() {
        // Создание интента для отправки данных
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseUrl)
            type = "text/plain"
        }

        // Запуск стандартного системного sharing-диалога
        startActivity(Intent.createChooser(sendIntent, "Поделиться приложением"))
    }

    private fun sendEmailToSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(studentEmail))
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
        }

        startActivity(intent)
    }

}