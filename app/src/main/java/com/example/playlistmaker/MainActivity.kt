package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = findViewById<Button>(R.id.settingB)
        val search = findViewById<Button>(R.id.searchB)
        val librar = findViewById<Button>(R.id.libraryB)

        settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        search.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        librar.setOnClickListener {
            val librarIntent = Intent(this, LibraryActivity::class.java)
            startActivity(librarIntent)
        }

    }
}