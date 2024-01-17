package com.example.playlistmaker.mediateka.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val MEDIA_SCREEN_TABS_COUNT : Int = 2
class MediaViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = MEDIA_SCREEN_TABS_COUNT
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}