package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.domain.StateSearch
import com.example.playlistmaker.search.domain.TrackSearch


class SearchActivity : AppCompatActivity() {

    private val searchedSong = ArrayList<TrackSearch>()
    private val clickedSong = ArrayList<TrackSearch>()
    private val searchMusicAdapter = TrackAdapter(searchedSong) {trackClickListener(it)}
    private val clickedMusicAdapter = TrackAdapter(clickedSong) {trackClickListener(it)}
    private val handler = Handler(Looper.getMainLooper()) // его  от сюда убрать?
    private var searchText = ""
    private var clickAllowed = true
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PAN_SearchActivity", "SearchActivity onCreate")

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.stateLiveData().observe(this) {
            updateScreen(it)
            Log.d("PAN_SearchActivity", "Изменения статуса во ViewModel ${this.toString()}")
        }

        //Слушатель НАЗАД
        binding.backButton.setOnClickListener {
            finish()
        }

        // Слушатель Done
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(searchText, true)
                binding.groupSearched.isVisible = true
            }
            false
        }

        // Слушатель Обновить
        binding.zaglushkaInetButton.setOnClickListener {
            viewModel.searchDebounce(searchText, true)
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
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearch.adapter = searchMusicAdapter

        // Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked
        binding.recyclerViewClicked.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewClicked.adapter = clickedMusicAdapter


        // изменения текста в поле поиска. Привязка обьекта TextWatcher
        fun textWatcher() = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { }
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
                viewModel.searchDebounce(searchText, false)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        provideTextWatcher(textWatcher())

    } // The END  fun onCreate

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

    // запоминание текста поисковой строки inputSearchText в переменную
    @SuppressLint("SuspiciousIndentation")
    override fun onSaveInstanceState(outState: Bundle) {
        val inputSearchText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(SEARCH_STRING, inputSearchText.text.toString())
        super.onSaveInstanceState(outState)
    }

    //заполнение тектового поля из предыдущего запуска Активити
    @SuppressLint("SuspiciousIndentation")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(SEARCH_STRING)) {
            val searchText = savedInstanceState.getString(SEARCH_STRING)
            binding.inputEditText.setText(searchText)
        }
    }

    private fun trackClickListener(track: TrackSearch) {
        if (isClickAllowed()) {
            viewModel.addTrackToHistory(track)
            goToPlayer(track.trackId.toString())
        }
    }
    fun goToPlayer(trackId: String) {
        val playerIntent = Intent(this, MediaActivity::class.java)
        playerIntent.putExtra(MediaStore.Audio.AudioColumns.TRACK, trackId)
        startActivity(playerIntent)
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, SEARCH_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun updateScreen(state: StateSearch) {
        binding.apply {

            Log.d ("PAN_SearchActivity", state.toString())
            when (state) {
                is StateSearch.Content -> {
                    Log.d ("PAN_SearchActivity", "Выполняем Content")
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
                    Log.d ("PAN_SearchActivity", "Выполняем Error")
                    progressBar.isVisible = false
                    groupSearched.isVisible = true
                    recyclerViewSearch.isVisible = false
                    zaglushkaPustoi.isVisible = false
                    nothingWasFound.isVisible = false
                    zaglushkaInetProblem.isVisible = true
                    textInetProblem.isVisible = true

                }

                is StateSearch.Empty -> {
                    Log.d ("PAN_SearchActivity", "Выполняем Empty")
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
                    Log.d ("PAN_SearchActivity", "Выполняем Loading")
                    groupClicked.isVisible = false
                    groupSearched.isVisible = false
                    progressBar.isVisible = true
                    zaglushkaInetProblem.isVisible = false
                    textInetProblem.isVisible = false
                }

                is StateSearch.ContentHistoryList -> {
                    Log.d ("PAN_SearchActivity", "Выполняем ContentHistoryList")
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
                    Log.d ("PAN_SearchActivity", "Выполняем EmptyHistoryList")
                    groupClicked.isVisible = false
                }
            }
        }
    }
}





