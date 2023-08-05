package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {
    private var inputEditText: String = ""
    private var valueString: String = ""


    private val searchSongs = mutableListOf<Track>() // песни найденные через iTunesApi
    private var clickedSearchSongs = arrayListOf<Track>() // песни сохраненные по клику

    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val appleApiService = retrofit.create(AppleApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val linearLayout = findViewById<FrameLayout>(R.id.container)


        val inputEditText = findViewById<EditText>(R.id.inputEditText) //  EditText поиска песен
        val clearButton = findViewById<ImageView>(R.id.clearIcon) // крестик очистки EditText
        val backButton = findViewById<Button>(R.id.backButton) //нажатие на стрелку НАЗАД

        val noSongImage = findViewById<ImageView>(R.id.zaglushka_pustoi)
        val zaglushkaPustoiText = findViewById<TextView>(R.id.zaglushka_pustoi_text)
        val inetProblemImage =
            findViewById<Button>(R.id.zaglushka_inet_button) // ImageView показа отсутствия интернета

        //val trackAdapter = TrackAdapter(searchSongs)

        val recyclerViewSearch =
            findViewById<RecyclerView>(R.id.recyclerViewSearch)  // Recycler найденных песен
        val recyclerViewClicked =
            findViewById<RecyclerView>(R.id.recyclerViewClicked)   // Recycler сохраненных песен

        val groupSearched =
            findViewById<LinearLayout>(R.id.group_searched)     // контейнер с найденными трэками
        val groupClicked =
            findViewById<LinearLayout>(R.id.group_clicked)  // контейнер с сохраненными трэками
        val clearHistory = findViewById<Button>(R.id.clear_history)  // кнопка Очистить историю

        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)
        clickedSearchSongs = sharedPrefsUtils.readClickedSearchSongs(CLICKED_SEARCH_TRACK)


        fun searchTracks() {
            appleApiService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TracksResponse> {

                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == 200) {
                            searchSongs.clear()
                            recyclerViewSearch.adapter?.notifyDataSetChanged()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                noSongImage.visibility = GONE
                                zaglushkaPustoiText.visibility = GONE
                                inetProblemImage.visibility = GONE
                                searchSongs.addAll(response.body()?.results!!)
                                recyclerViewSearch.adapter?.notifyDataSetChanged()
                            } else {
                                runOnUiThread {
                                    searchSongs.clear()
                                    recyclerViewSearch.adapter?.notifyDataSetChanged()
                                    zaglushkaPustoiText.setText(R.string.error_not_found)
                                    noSongImage.setImageResource(R.drawable.empty_mode)
                                    noSongImage.visibility = VISIBLE
                                    zaglushkaPustoiText.visibility = VISIBLE
                                    inetProblemImage.visibility = GONE
                                }
                            }
                        } else {
                            searchSongs.clear()
                            recyclerViewSearch.adapter?.notifyDataSetChanged()
                            runOnUiThread {
                                zaglushkaPustoiText.setText(R.string.error_not_internet)
                                noSongImage.setImageResource(R.drawable.error_mode)
                                noSongImage.visibility = VISIBLE
                                zaglushkaPustoiText.visibility = VISIBLE
                                inetProblemImage.visibility = VISIBLE
                            }
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        runOnUiThread {
                            searchSongs.clear()
                            recyclerViewSearch.adapter?.notifyDataSetChanged()
                            zaglushkaPustoiText.setText(R.string.error_not_internet)
                            noSongImage.setImageResource(R.drawable.error_mode)
                            noSongImage.visibility = VISIBLE
                            zaglushkaPustoiText.visibility = VISIBLE
                            inetProblemImage.visibility = VISIBLE
                        }
                    }

                })
        }

        fun showGroupClickedSong() {
            if (clickedSearchSongs.size > 0) {
                groupSearched.visibility =
                    if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) GONE else VISIBLE
                groupClicked.visibility =
                    if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) VISIBLE else GONE
            } else {
                groupSearched.visibility = VISIBLE
                groupClicked.visibility = GONE
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            // если будут изменения текста в поле поиска, то крестик очистки появится, при удалении - станет невидимым
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    clearButton.visibility = GONE
                } else {
                    clearButton.visibility = VISIBLE
                }
                showGroupClickedSong()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        backButton.setOnClickListener {
            finish()
        }


        // при нажатии на крестик очистки поля поиска:
        clearButton.setOnClickListener {
            inputEditText.setText("")
            noSongImage.visibility = GONE
            inetProblemImage.visibility = GONE
            searchSongs.clear()
            recyclerViewSearch.adapter?.notifyDataSetChanged()
            recyclerViewClicked.adapter?.notifyDataSetChanged()
        }


        inputEditText.addTextChangedListener(simpleTextWatcher)



        // при получении фокуса показать историю просмотренных песен
        inputEditText.setOnFocusChangeListener { view, hasFocus -> showGroupClickedSong() }

        // обработка нажатия на кнопку Done
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searchTracks()
                true
                groupSearched.visibility = VISIBLE
            }
            false
        }

        // обработка нажатия на кнопку Обновить
        inetProblemImage.setOnClickListener {
            noSongImage.visibility = GONE
            zaglushkaPustoiText.visibility = GONE
            inetProblemImage.visibility = GONE
            searchTracks()
        }

        // обработка нажатия на кнопку Очистить историю
        clearHistory.setOnClickListener {
            clickedSearchSongs.clear()

            sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)
            showGroupClickedSong()
            recyclerViewClicked.adapter?.notifyDataSetChanged()
        }


        /*     Формирование списка найденных песен в recyclerViewSearch                 */
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        recyclerViewSearch.adapter = TrackAdapter(searchSongs, this)
        /*     Формирование списка сохраненных (кликнутых) песен в recyclerViewClicked  */
        recyclerViewClicked.layoutManager = LinearLayoutManager(this)
        recyclerViewClicked.adapter = ClickedMusicAdapter(clickedSearchSongs, this)

        // КОНЕЦ  fun onCreate(savedInstanceState: Bundle?)

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(PRODUCT_AMOUNT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText = savedInstanceState.getString(PRODUCT_AMOUNT, "")
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        Log.e("PRODUCT_AMOUNT", "ValueString before $valueString")
        valueString = savedInstanceState.getString(PRODUCT_AMOUNT, "")
        Log.e("PRODUCT_AMOUNT", "ValueString after $valueString")
        inputEditText.setText(valueString)
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

        if (clickedSearchSongs.contains(clickedTrack)) {
            clickedSearchSongs.remove(clickedTrack)
        } else if (clickedSearchSongs.size >= 10) {
            clickedSearchSongs.removeAt(clickedSearchSongs.size - 1)
        }
        clickedSearchSongs.add(0, clickedTrack)
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        val sharedPrefsUtils = SharedPrefsUtils(sharedPrefsApp)

        sharedPrefsUtils.writeClickedSearchSongs(CLICKED_SEARCH_TRACK, clickedSearchSongs)
       /* val displayIntent = Intent(this, MediaActivity::class.java)
        displayIntent.putExtra("trackId", clickedTrack.trackId)
        startActivity(displayIntent)*/
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

}






