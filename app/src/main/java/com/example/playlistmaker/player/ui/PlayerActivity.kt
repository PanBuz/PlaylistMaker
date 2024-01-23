package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackSearch
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<MediaViewModel>()
    private lateinit var buttonPlay: MaterialButton
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("PAN_MediaActivity", "MediaActivity onCreate")

        buttonPlay = binding.btPlay

        viewModel.observScreen().observe(this) { refreshScreen(it) }

        viewModel.observTimer().observe(this) { binding.tvPlaybackTime.text = it }

        placeInPlace(viewModel.getTrack())

        buttonPlay.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding.ivBack.setOnClickListener { finish() }

    }

    //Устанавливаем вьюхи согласно теме
    private fun refreshScreen(state: PlayerState) {
        when (state) {
            is PlayerState.PLAYING -> {
                buttonPlay.setIconResource(R.drawable.button_pause)
            }

            is PlayerState.PAUSED -> {
                buttonPlay.setIconResource(R.drawable.button_play)
            }

            is PlayerState.PREPARE -> {
                buttonPlay.setIconResource(R.drawable.button_play)
                buttonPlay.isEnabled = true
            }

            else -> {
                buttonPlay.setIconResource(R.drawable.button_play)
            }
        }

    }

    private fun placeInPlace(playedTrack: TrackSearch) {
        binding.apply {
            tvTitle.setText(playedTrack.trackName)
            tvArtist.setText(playedTrack.artistName.toString())
            tvPlaybackTime.setText(R.string.null_time)
            tvDuration.setText(playedTrack.durationTrack)
            tvAlbum.setText(playedTrack.collectionName)
            tvYear.setText(playedTrack.releaseDate.substring(0, 4))
            tvGenre.setText(playedTrack.primaryGenreName)
            tvCountry.setText(playedTrack.country)
        }

        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        Glide.with(binding.ivCover512)
            .load(playedTrack.coverUrl500)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivCover512)
        buttonPlay.isEnabled = false
    }

}
