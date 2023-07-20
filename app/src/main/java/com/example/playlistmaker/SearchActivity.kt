package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var inputEditText: String =""
    private var valueString: String = ""

    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val linearLayout = findViewById<FrameLayout>(R.id.container)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backButton = findViewById<Button>(R.id.backButton)
        val tracks = mutableListOf<Track>()
        val zaglushkaPustoi = findViewById<ImageView>(R.id.zaglushka_pustoi)
        val zaglushkaPustoiText = findViewById<TextView>(R.id.zaglushka_pustoi_text)
        val zaglushkaInetButton = findViewById<Button>(R.id.zaglushka_inet_button)

        val trackAdapter = TrackAdapter(tracks)

        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        rvTrack.adapter = trackAdapter
        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            zaglushkaPustoi.visibility = GONE
            zaglushkaPustoiText.visibility = GONE
            zaglushkaInetButton.visibility = GONE
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                valueString = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        val appleApiService = retrofit.create(AppleApiService::class.java)
        fun searchTracks() {
            appleApiService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TracksResponse> {

                    @SuppressLint("ResourceAsColor")
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            trackAdapter.notifyDataSetChanged()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                zaglushkaPustoi.visibility = GONE
                                zaglushkaPustoiText.visibility = GONE
                                zaglushkaInetButton.visibility = GONE
                                tracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                runOnUiThread {
                                    tracks.clear()
                                    trackAdapter.notifyDataSetChanged()
                                    zaglushkaPustoiText.setText(R.string.error_not_found)
                                    zaglushkaPustoi.setImageResource(R.drawable.empty_mode)
                                    zaglushkaPustoi.visibility = VISIBLE
                                    zaglushkaPustoiText.visibility = VISIBLE
                                    zaglushkaInetButton.visibility = GONE
                                }
                            }
                        } else {
                            tracks.clear()
                            trackAdapter.notifyDataSetChanged()
                            runOnUiThread {
                                zaglushkaPustoiText.setText(R.string.error_not_internet)
                                zaglushkaPustoi.setImageResource(R.drawable.error_mode)
                                zaglushkaPustoi.visibility = VISIBLE
                                zaglushkaPustoiText.visibility = VISIBLE
                                zaglushkaInetButton.visibility = VISIBLE
                            }
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        runOnUiThread {
                            tracks.clear()
                            trackAdapter.notifyDataSetChanged()
                            zaglushkaPustoiText.setText(R.string.error_not_internet)
                            zaglushkaPustoi.setImageResource(R.drawable.error_mode)
                            zaglushkaPustoi.visibility = VISIBLE
                            zaglushkaPustoiText.visibility = VISIBLE
                            zaglushkaInetButton.visibility = VISIBLE
                        }
                    }

                })
        }


        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ

                searchTracks()
                true
            }
            false
        }

        zaglushkaInetButton.setOnClickListener {
            zaglushkaPustoi.visibility = GONE
            zaglushkaPustoiText.visibility = GONE
            zaglushkaInetButton.visibility = GONE
            searchTracks()
        }

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
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

}






