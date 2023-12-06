package com.example.playlistmaker.setting.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity() :  AppCompatActivity() {

    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PAN_SettingsActivity", "SettingsActivity onCreate")

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            SettingViewModel.getViewModelFactory())[SettingViewModel::class.java]

        binding.themeSw.isChecked = viewModel.getThemeState() // изменяем тему приложения

        viewModel.themeLiveData.observe(this) {
                checked -> binding.themeSw.isChecked = checked  } // Подписка на изменение данных themeLiveData

        binding.themeSw.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked) }                      // Слушатель переключения темы

        binding.backSettingsBtm.setOnClickListener { finish() } // НАЗАД

        binding.shareBtm.setOnClickListener { viewModel.shareApp() } // ПОДЕЛИТЬСЯ


        binding.supportBtm.setOnClickListener { viewModel.writeInSupport() } //НАПИСАТЬ в ПОДДЕРЖКУ

        binding.licenceBtm.setOnClickListener { viewModel.openUserTerms() }  //ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
    }

}
