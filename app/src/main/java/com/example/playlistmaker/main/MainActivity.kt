package com.example.playlistmaker.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_main)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, nd, _ ->
            bottomNavigationView.isVisible =
                ! (nd.id == R.id.newPlaylistFragment || nd.id == R.id.playerFragment ||
                        nd.id==R.id.displayPlaylist || nd.id==R.id.updatePlaylistFragment)
        }

    }
}