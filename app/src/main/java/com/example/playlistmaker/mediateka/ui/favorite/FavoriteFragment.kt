package com.example.playlistmaker.mediateka.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoriteViewModel>()
    private val favoriteSong = ArrayList<TrackSearch>()
    private val favoriteMusicAdapter = TrackAdapter(favoriteSong) { trackClickListener(it) }
    private lateinit var trackClickListener: (TrackSearch) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favoriteLiveData.observe(viewLifecycleOwner) { state ->
            updateFavorite(state)
        }

        binding.recyclerViewFavorited.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorited.adapter = favoriteMusicAdapter


        trackClickListener = debounce(
            SearchFragment.CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            viewModel.setClickedTrack(track, this)
            runPlayer(track.trackId.toString())
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun runPlayer(trackId: String) {
        findNavController().navigate(R.id.playerFragment)
    }

    private fun updateFavorite(state: FavoriteState) {
        binding.apply {

            when (state) {
                is FavoriteState.Content -> {
                    binding.ivEmptyFavorite.isVisible = false
                    binding.tvEmptyFavorite.isVisible = false
                    binding.groupFavorited.isVisible = true

                    favoriteMusicAdapter.trackData.clear()
                    state.tracks.map { trackModel -> trackModel.isFavorite = true }
                    favoriteMusicAdapter.trackData.addAll(state.tracks)
                    favoriteMusicAdapter.notifyDataSetChanged()
                }

                else -> {
                    binding.groupFavorited.isVisible = false
                    binding.ivEmptyFavorite.isVisible = true
                    binding.tvEmptyFavorite.isVisible = true
                }
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}