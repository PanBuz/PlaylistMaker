package com.example.playlistmaker.mediateka.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding

class MediatekaFragment : Fragment()  {

    private var _binding: FragmentMediatekaBinding? = null
    private val binding get() = _binding!!
    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator get() = _tabMediator!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentMediatekaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

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
        //_tabMediator?.detach()
        _binding = null
    }
}