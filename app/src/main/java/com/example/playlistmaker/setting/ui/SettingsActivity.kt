package com.example.playlistmaker.setting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity :  AppCompatActivity() {

    private val viewModel by viewModel<SettingViewModel>()
    private lateinit var binding: ActivitySettingsBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PAN_SettingsActivity", "SettingsActivity onCreate")

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.themeLiveData.observe(this) {
               // checked -> binding.themeSw.isChecked = checked
            Log.d("PAN_SettingsActivity", "SettingsActivity onCreate3")
            } // Подписка на изменение данных themeLiveData

        binding.themeSw.isChecked = viewModel.getThemeState() // изменяем тему приложения


        binding.themeSw.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked) }                      // Слушатель переключения темы

        binding.backSettingsBtm.setOnClickListener { finish() } // НАЗАД

        binding.shareBtm.setOnClickListener { viewModel.shareApp() } // ПОДЕЛИТЬСЯ


        binding.supportBtm.setOnClickListener { viewModel.writeInSupport() } //НАПИСАТЬ в ПОДДЕРЖКУ

        binding.licenceBtm.setOnClickListener { viewModel.openUserTerms() }  //ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
    }

}
