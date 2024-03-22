package com.example.playlistmaker.mediateka.ui.displayPlaylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentDisplayPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.Converters
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DisplayPlaylistFragment : Fragment() {

    private var _binding: FragmentDisplayPlaylistBinding? = null
    private val binding get() = _binding !!
    private val viewModel by viewModel<DisplayPlaylistViewModel>()
    private lateinit var trackClickListener: (TrackSearch) -> Unit
    private val adapter = TrackAdapter(arrayListOf<TrackSearch>(), {trackClickListener(it)})
    private lateinit var bottomSheetBehaviorMenu : BottomSheetBehavior <LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTracksOfPlaylist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTracksOfPlaylist.adapter = adapter
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        // 1. Получить аргументы с предыдущего экрана (idPl с PlaylistFragment)
        val idPl = requireArguments().getInt("PLAYLIST")

        // 2. Получить плэйлист, соответсвующий idPl (из БД)
        viewModel.getPlaylistById(idPl)

        // 3. Передать трэки плейлиста в адаптер и отобразить плэйлист
        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            val tracks = replaceCoverTrackToArtwork160(playlist.tracks)
            adapter.trackData.clear()
            adapter.trackData.addAll(tracks)
            adapter.notifyDataSetChanged()
            actualPlaylist = playlist
            displayPlaylist(actualPlaylist!!)
            Log.d("PAN_DisplayPlaylistFragment", "!!Изменился playlist= $playlist")
        }

        // 4. Обработать нажатие на трэк плэйлиста
        trackClickListener = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            viewModel.addTrackToHistory(track)
            runPlayer(track.trackId.toString())
        }

        // 5. Обработать длинное нажатие на трэк плэйлиста
        adapter.onLongClickListener = { track ->
            deleteTrackDialog(track.trackId).show()
            true
        }

        // 6. Нажатие на кнопку НАЗАД
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
            //MediaFragment().binding.viewPager.currentItem = 1
        }

        // Нажатие на кнопку МЕНЮ (...)
        binding.ibMenu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            displaybottomSheetBehaviorMenu ()
        }

        // Нажатие на кнопку ПОДЕЛИТЬСЯ
        binding.ibShare.setOnClickListener {
            shareDialog()
        }

        // Выбор меню ПОДЕЛИТЬСЯ
        binding.shareBottomSheet.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            shareDialog()
        }

        // Выбор меню РЕДАКТИРОВАТЬ ИНФОРМАЦИЮ
        binding.updatePlBottomSheet.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            // новый путь к файлу
            val imagePl = viewModel.imagePath() + "/" + actualPlaylist!!.name + ".jpg"
            val bundle : Bundle = bundleOf()
            bundle.putInt("idPl", idPl)
            bundle.putString("namePl",actualPlaylist!!.name )
            bundle.putString("imagePl", imagePl )
            bundle.putString("descriptPl",actualPlaylist!!.descript )
            findNavController().navigate(R.id.updatePlaylistFragment, bundle)
        }

        // Выбор меню УДАЛИТЬ ПЛЭЙЛИСТ
        binding.deletePlBottomSheet.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistDialog (actualPlaylist !!.id).show()
        }
    }
    override fun onResume() {
        super.onResume()
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
    }
    private fun displayPlaylist(playlist:Playlist) {
        binding.apply {
            tvNamePl.text = playlist.name
            tvDesciptPl.text = playlist.descript
            tvPlaylistCount.text = Converters(requireContext()).convertCountToTextTracks (playlist.tracks.size)
            tvPlaylistTime.text = (playlistTime (playlist))
        }
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        val coverPlaylist = viewModel.imagePath() +"/"+ playlist.name + ".jpg"
        Glide.with(binding.ivCoverPlaylist)
            .load(coverPlaylist)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.album)
            //.transform(RoundedCorners(radius))
            .into(binding.ivCoverPlaylist)
    }

    private fun displaybottomSheetBehaviorMenu (){
        val countTracks = Converters(requireContext()).convertCountToTextTracks (actualPlaylist!!.countTracks)
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        binding.namePlBottomSheet.text = actualPlaylist!!.name
        binding.tracksBottomSheet.text = countTracks
        val coverPlaylist = viewModel.imagePath() +"/"+ actualPlaylist!!.name + ".jpg"
        Glide.with(binding.ivImageplBottomSheet)
            .load(coverPlaylist)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivImageplBottomSheet)
    }

    private fun shareDialog() {
        val titlePlaylist = ""
        if (actualPlaylist!!.tracks.size<1) {
            var sharedPlaylist = getString(R.string.no_track_to_share)
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(sharedPlaylist) // Заголовок диалога
                .setPositiveButton(R.string.its_clear) { dialog, which ->    }
                .show()
        } else {
            viewModel.sharePlaylist(createPlaylist(actualPlaylist !!), titlePlaylist)
        }
    }

    private fun runPlayer(trackId: String) {
        findNavController().navigate(R.id.playerFragment)
    }

    private fun playlistTime (playlist: Playlist) : String {
        var result = 0L
        for (track in playlist.tracks) {
            result += track.trackTimeMillis
        }
        return Converters(requireContext()).convertMillisToTextMinutes(result)
    }

    private fun deletePlaylistDialog(idPl: Int) = MaterialAlertDialogBuilder(requireActivity())
        .setTitle(R.string.delete_playlist)
        .setMessage(R.string.really_delete_playlist)
        .setNeutralButton(getString(R.string.cancel_button)) { _, _ -> }
        .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
            deletePlaylistById(idPl )
        }

    private fun deleteTrackDialog(trackId: String) = MaterialAlertDialogBuilder(requireActivity())
        .setTitle(R.string.delete_track)
        .setMessage(R.string.really_delete_track)
        .setNeutralButton(getString(R.string.cancel_button)) { _, _ -> }
        .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
            deleteTrackFromPlaylist(trackId, actualPlaylist!!.id )
        }

    private fun deletePlaylistById(idPl : Int) {
        viewModel.deletePl(idPl)
        findNavController().navigateUp()
    }

    private fun deleteTrackFromPlaylist(trackId: String, idPl : Int) {
        viewModel.deleteTrackFromPlaylist(trackId, idPl )
        viewModel.getPlaylistById(idPl)
    }

    private fun createPlaylist(playlist: Playlist) : String {
        var sharedPlaylist = getString(R.string.playlist) +": " +playlist.name + "\n " +
                playlist.descript + "\n " +
                Converters(requireContext()).convertCountToTextTracks(playlist.countTracks) + "\n "
        for ( i in 1 ..  (playlist.tracks.size)) {
            sharedPlaylist = sharedPlaylist + "$i. " + playlist.tracks[i-1].artistName + " - " +
                    playlist.tracks[i-1].trackName + " - " +
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format (playlist.tracks[i-1].trackTimeMillis) + "\n"
        }
        return sharedPlaylist
    }

    private fun replaceCoverTrackToArtwork160(tracks : ArrayList<TrackSearch>) : ArrayList<TrackSearch> {
        for (track in tracks) {
            val coverUrl100 = track.artworkUrl100
            val coverUrl60 = coverUrl100.replaceAfterLast('/', "60x60bb.jpg")
            track.artworkUrl100 = coverUrl60
        }
        return tracks
    }

    companion object {
        fun passArgs(playlistId: Int): Bundle = bundleOf("PLAYLIST" to playlistId)
        var actualPlaylist : Playlist? = Playlist(0, "", "", "", arrayListOf(), 0, 0L)
    }
}