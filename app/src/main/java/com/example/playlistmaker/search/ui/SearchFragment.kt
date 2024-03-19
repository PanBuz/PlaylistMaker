package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding

import com.example.playlistmaker.search.domain.StateSearch
import com.example.playlistmaker.search.domain.TrackSearch
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val searchedSong = ArrayList<TrackSearch>()
    private val clickedSong = ArrayList<TrackSearch>()
    private val searchMusicAdapter = TrackAdapter(searchedSong) { trackClickListener(it) }
    private val clickedMusicAdapter = TrackAdapter(clickedSong) { trackClickListener(it) }
    private var searchText = ""
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var trackClickListener: (TrackSearch) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PAN_SearchActivity", "SearchActivity onCreate")


        viewModel.stateLiveData().observe(viewLifecycleOwner) {
            updateScreen(it)
            Log.d("PAN_SearchActivity", "Изменения статуса во ViewModel ${this.toString()}")
        }


        // Слушатель Done
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchText)
                binding.groupSearched.isVisible = true
            }
            false
        }

        // Слушатель Обновить
        binding.zaglushkaInetButton.setOnClickListener {
            viewModel.searchDebounce(searchText)
        }

        // Слушатель Очистить историю
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            viewModel.getTracksHistory()
            binding.groupClicked.isVisible = false
            binding.recyclerViewClicked.adapter?.notifyDataSetChanged()
        }

        // Слушатель крестик очистки поля поиска:
        binding.apply {
            clearIcon.setOnClickListener {
                searchedSong.clear()
                viewModel.getTracksHistory()
                searchMusicAdapter.notifyDataSetChanged()
                recyclerViewSearch.adapter?.notifyDataSetChanged()
                recyclerViewClicked.adapter?.notifyDataSetChanged()
                inputEditText.setText("")
                zaglushkaPustoi.isVisible = false
                nothingWasFound.isVisible = false
                clearIcon.isVisible = false
                groupClicked.isVisible = true
                recyclerViewClicked.isVisible = true
            }
        }


        // Формирование списка найденных песен в recyclerViewSearch
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearch.adapter = searchMusicAdapter

        // Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked
        binding.recyclerViewClicked.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewClicked.adapter = clickedMusicAdapter


        // изменения текста в поле поиска. Привязка обьекта TextWatcher
        fun textWatcher() = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            // изменения текста в поле поиска в реальном времени
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = false
                if (s.isNullOrEmpty()) {
                    binding.clearIcon.isVisible = false
                } else {
                    binding.clearIcon.isVisible = true
                }
                binding.groupClicked.isVisible = false
                searchText = s.toString()
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        provideTextWatcher(textWatcher())

        trackClickListener =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.addTrackToHistory(track)
                goToPlayer(track.trackId.toString())
            }

        binding.inputEditText.requestFocus()

    }

    fun provideTextWatcher(textWatcher: TextWatcher) {
        binding.inputEditText.apply {
            addTextChangedListener(textWatcher)
            setText(searchText)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && this.text.isEmpty())
                    viewModel.getTracksHistory()
                else
                    binding.groupClicked.isVisible = false
            }
        }
    }


    fun goToPlayer(trackId: String) {
       // val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
       // playerIntent.putExtra(MediaStore.Audio.AudioColumns.TRACK, trackId)
       // startActivity(playerIntent)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
    }


    private fun updateScreen(state: StateSearch) {
        binding.apply {

            Log.d("PAN_SearchActivity", state.toString())
            when (state) {
                is StateSearch.Content -> {
                    Log.d("PAN_SearchActivity", "Выполняем Content")
                    searchedSong.clear()
                    searchedSong.addAll(state.tracks as ArrayList<TrackSearch>)
                    groupClicked.isVisible = false
                    progressBar.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = true
                    zaglushkaPustoi.isVisible = false
                    nothingWasFound.isVisible = false
                    zaglushkaInetProblem.isVisible = false
                    textInetProblem.isVisible = false
                    searchMusicAdapter.notifyDataSetChanged()
                }

                is StateSearch.Error -> {
                    Log.d("PAN_SearchActivity", "Выполняем Error")
                    progressBar.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = false
                    zaglushkaPustoi.isVisible = false
                    nothingWasFound.isVisible = false
                    zaglushkaInetProblem.isVisible = true
                    textInetProblem.isVisible = true

                }

                is StateSearch.Empty -> {
                    Log.d("PAN_SearchActivity", "Выполняем Empty")
                    progressBar.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = false
                    zaglushkaPustoi.isVisible = true
                    nothingWasFound.isVisible = true
                    zaglushkaInetButton.isVisible = false
                    zaglushkaInetProblem.isVisible = false
                    textInetProblem.isVisible = false
                }

                is StateSearch.Loading -> {
                    Log.d("PAN_SearchActivity", "Выполняем Loading")
                    groupClicked.isVisible = false
                    groupSearched.isVisible = false
                    progressBar.isVisible = true
                    zaglushkaInetProblem.isVisible = false
                    textInetProblem.isVisible = false
                }

                is StateSearch.ContentHistoryList -> {
                    Log.d("PAN_SearchActivity", "Выполняем ContentHistoryList")
                    groupClicked.isVisible = true
                    progressBar.isVisible = false
                    groupSearched.isVisible = false
                    recyclerViewClicked.isVisible = true
                    zaglushkaInetProblem.isVisible = false
                    textInetProblem.isVisible = false
                    clickedSong.clear()
                    clickedSong.addAll(state.historyList)
                    clickedMusicAdapter.notifyDataSetChanged()
                }

                is StateSearch.EmptyHistoryList -> {
                    Log.d("PAN_SearchActivity", "Выполняем EmptyHistoryList")
                    groupClicked.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}





