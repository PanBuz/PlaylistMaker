package com.example.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.mediateka.ui.MediatekaActivity
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.setting.ui.SettingsActivity


class MainActivity : AppCompatActivity() {


    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)


        binding?.settingButton?.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        binding?.searchButton?.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding?.libraryButton?.setOnClickListener {
            val librarIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(librarIntent)
        }

    }
}