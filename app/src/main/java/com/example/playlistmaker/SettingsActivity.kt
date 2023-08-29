package com.example.playlistmaker

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.net.Uri
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val studentEmail = "aleksandor1203@yandex.ru"
    private var binding : ActivitySettingsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val themeSwitcher = binding?.themeSw
        val backSettingsButton = binding?.backSettingsBtm
        val shareButton = binding?.shareBtm
        val supportButton = binding?.supportBtm
        val licenceBottom: Button? = binding?.licenceBtm

        //изменение темы приложения
        val sharedPrefs = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)

        themeSwitcher?.setChecked (sharedPrefs.getString (DARK_THEME_ENABLED, "false").toBoolean())
        themeSwitcher?.setOnCheckedChangeListener { switcher, checked ->
            sharedPrefs.edit()
                .putString(DARK_THEME_ENABLED, checked.toString())
                .apply()
            (applicationContext as App).switchTheme(checked)
        }


        backSettingsButton?.setOnClickListener {
            finish()
        }

        // Обработчик нажатия на кнопку "Поделиться приложением"
        shareButton?.setOnClickListener {
            shareCourse()
        }
        //кнопка поддержки
        supportButton?.setOnClickListener {
            sendEmailToSupport()
        }
        //почитать лицензию
        licenceBottom?.setOnClickListener {
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
            val shareText = getResources().getString(R.string.share_text)
            putExtra(Intent.EXTRA_TEXT, courseUrl)
            type = "text/plain"
            startActivity(Intent.createChooser(this, shareText))
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