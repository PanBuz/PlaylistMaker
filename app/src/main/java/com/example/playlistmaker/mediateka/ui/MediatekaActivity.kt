package com.example.playlistmaker.mediateka.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker.R

class MediatekaActivity : AppCompatActivity() {

    private var _binding: ActivityMediatekaBinding? = null
    private val binding get() = _binding!!
    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator get() = _tabMediator!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.btBack.setOnClickListener {
            finish()
        }

        _tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setText( R.string.favorit_tracks)
                1 -> tab.setText(R.string.playlists)
            }
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _tabMediator?.detach()
    }
}