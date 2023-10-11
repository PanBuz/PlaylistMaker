package com.example.playlistmaker.presentation.Ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.domain.App
import com.example.playlistmaker.data.network.AppleApiService
import com.example.playlistmaker.domain.CLICKED_SEARCH_TRACK
import com.example.playlistmaker.domain.MUSIC_MAKER_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SharedPrefsUtils
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.presentation.TrackAdapter
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.presentation.ClickedMusicAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
    private var inputEditText: String = ""
    private var valueString: String = ""
    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 1500L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private var lastClickTime = 0L
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper()) // для запроса


    private val searchSongs = mutableListOf<Track>() // песни найденные через iTunesApi
    private var clickedSearchSongs = arrayListOf<Track>() // песни сохраненные по клику

    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val appleApiService = retrofit.create(AppleApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayout = binding.container // не используется?

        val etInputSearchText = binding.inputEditText //  EditText поиска песен
        val btClearButton = binding.clearIcon  // крестик очистки EditText
        val btBackButton = binding.backButton //нажатие на стрелку НАЗАД

        val ivNoSongImage = binding.zaglushkaPustoi
        val tvZaglushkaPustoiText = binding.zaglushkaPustoiText
        val ivInetProblemImage = binding.zaglushkaInetButton// ImageView показа отсутствия интернета

        val rvSearch = binding.recyclerViewSearch  // Recycler найденных песен
        val rvClicked = binding.recyclerViewClicked   // Recycler сохраненных песен

        val llGroupSearched = binding.groupSearched    // контейнер с найденными трэками
        val llGroupClicked = binding.groupClicked // контейнер с сохраненными трэками
        val btClearHistory = binding.clearHistory  // кнопка Очистить историю
        val progressBar = binding.progressBar // ПрогрессБар

        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)
        clickedSearchSongs = sharedPrefsUtils.readClickedSearchSongs(CLICKED_SEARCH_TRACK)


        fun searchTracks() {
            if (etInputSearchText.text.isNotEmpty()) {
                // Меняем видимость элементов перед выполнением запроса
                tvZaglushkaPustoiText.visibility = GONE
                rvClicked.visibility = GONE
                progressBar.visibility = VISIBLE

                appleApiService.search(etInputSearchText.text.toString())
                    .enqueue(object : Callback<TracksResponse> {

                        @SuppressLint("ResourceAsColor")
                        override fun onResponse(
                            call: Call<TracksResponse>,
                            response: Response<TracksResponse>
                        ) {
                            progressBar.visibility =
                                GONE // Прячем ProgressBar после успешного выполнения запроса
                            if (response.code() == 200) {
                                searchSongs.clear()
                                rvSearch.adapter?.notifyDataSetChanged()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    ivNoSongImage.visibility = GONE
                                    tvZaglushkaPustoiText.visibility = GONE
                                    ivInetProblemImage.visibility = GONE
                                    searchSongs.addAll(response.body()?.results!!)
                                    rvSearch.adapter?.notifyDataSetChanged()
                                } else {
                                    runOnUiThread {
                                        searchSongs.clear()
                                        rvSearch.adapter?.notifyDataSetChanged()
                                        tvZaglushkaPustoiText.setText(R.string.error_not_found)
                                        ivNoSongImage.setImageResource(R.drawable.empty_mode)
                                        ivNoSongImage.visibility = VISIBLE
                                        tvZaglushkaPustoiText.visibility = VISIBLE
                                        ivInetProblemImage.visibility = GONE
                                    }
                                }
                            } else {
                                searchSongs.clear()
                                rvSearch.adapter?.notifyDataSetChanged()
                                runOnUiThread {
                                    tvZaglushkaPustoiText.setText(R.string.error_not_internet)
                                    ivNoSongImage.setImageResource(R.drawable.error_mode)
                                    ivNoSongImage.visibility = VISIBLE
                                    tvZaglushkaPustoiText.visibility = VISIBLE
                                    ivInetProblemImage.visibility = VISIBLE
                                }
                            }
                        }

                        @SuppressLint("ResourceAsColor")
                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                            progressBar.visibility =
                                View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                            runOnUiThread {
                                searchSongs.clear()
                                rvSearch.adapter?.notifyDataSetChanged()
                                tvZaglushkaPustoiText.setText(R.string.error_not_internet)
                                ivNoSongImage.setImageResource(R.drawable.error_mode)
                                ivNoSongImage.visibility = VISIBLE
                                tvZaglushkaPustoiText.visibility = VISIBLE
                                ivInetProblemImage.visibility = VISIBLE
                            }
                        }

                    })
            }
        }

        // функция для запроса в реальном времени
        val searchRunnable = Runnable { searchTracks() }

        fun searchDebounce() {
            //handler.removeCallbacks(searchRunnable)
            etInputSearchText.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
        }

        fun showGroupClickedSong() {
            if (clickedSearchSongs.size > 0) {
                llGroupSearched.visibility =
                    if (etInputSearchText.hasFocus() && etInputSearchText.text.isEmpty()) GONE else VISIBLE
                llGroupClicked.visibility =
                    if (etInputSearchText.hasFocus() && etInputSearchText.text.isEmpty()) VISIBLE else GONE
            } else {
                llGroupSearched.visibility = VISIBLE
                llGroupClicked.visibility = GONE
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            // если будут изменения текста в поле поиска, то крестик очистки появится, при удалении - станет невидимым
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    btClearButton.visibility = GONE
                } else {
                    btClearButton.visibility = VISIBLE
                    searchDebounce()
                }
                showGroupClickedSong()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        btBackButton.setOnClickListener {
            finish()
        }


        // при нажатии на крестик очистки поля поиска:
        btClearButton.setOnClickListener {
            etInputSearchText.setText("")
            ivNoSongImage.visibility = GONE
            ivInetProblemImage.visibility = GONE
            searchSongs.clear()
            rvSearch.adapter?.notifyDataSetChanged()
            rvClicked.adapter?.notifyDataSetChanged()
        }


        etInputSearchText.addTextChangedListener(simpleTextWatcher)


        // при получении фокуса показать историю просмотренных песен
        etInputSearchText.setOnFocusChangeListener { view, hasFocus -> showGroupClickedSong() }

        // обработка нажатия на кнопку Done
        etInputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searchTracks()
                true
                llGroupSearched.visibility = VISIBLE
            }
            false
        }



        // обработка нажатия на кнопку Обновить
        ivInetProblemImage.setOnClickListener {
            ivNoSongImage.visibility = GONE
            tvZaglushkaPustoiText.visibility = GONE
            ivInetProblemImage.visibility = GONE
            searchTracks()
        }

        // обработка нажатия на кнопку Очистить историю
        btClearHistory.setOnClickListener {
            clickedSearchSongs.clear()

            sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)
            showGroupClickedSong()
            rvClicked.adapter?.notifyDataSetChanged()
        }


        /*     Формирование списка найденных песен в recyclerViewSearch                 */
        rvSearch.layoutManager = LinearLayoutManager(this)
        rvSearch.adapter = TrackAdapter(searchSongs, this)
        /*     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  */
        rvClicked.layoutManager = LinearLayoutManager(this)
        rvClicked.adapter = ClickedMusicAdapter(clickedSearchSongs, this)

        // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(PRODUCT_AMOUNT, inputEditText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        if (savedInstanceState.containsKey(PRODUCT_AMOUNT)) {
            val valueString = savedInstanceState.getString(PRODUCT_AMOUNT)
            inputEditText.setText(valueString)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    // нажатие на найденные песни в Recycler через SearchMusicAdapter
    @SuppressLint("SuspiciousIndentation")
    override fun onClickRecyclerItemView(clickedTrack: Track) {
        val currentTime = System.currentTimeMillis()
        // Проверяем, прошло ли необходимое время с момента последнего клика
        if (currentTime - lastClickTime >= CLICK_DEBOUNCE_DELAY) {
            // Выполняем действия по клику на трек
            handleTrackClick(clickedTrack)

            // Обновляем время последнего клика
            lastClickTime = currentTime
        }

    }

    private fun handleTrackClick(clickedTrack: Track) {
        if (clickedSearchSongs.contains(clickedTrack)) {
            clickedSearchSongs.remove(clickedTrack)
        } else if (clickedSearchSongs.size >= 10) {
            clickedSearchSongs.removeAt(clickedSearchSongs.size - 1)
        }
        clickedSearchSongs.add(0, clickedTrack)
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)

        sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)
        App.activeTracks.add(0, clickedTrack)
        val displayIntent = Intent(this, MediaActivity::class.java)
        displayIntent.putExtra("trackId", clickedTrack.trackId)
        startActivity(displayIntent)
    }


}






