package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.services.itunes.ITunesAPI
import com.example.playlistmaker.services.itunes.SongsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var inputTextValue = DEF_TEXT

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    val adapter = TrackAdapter()

    private lateinit var backButton: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var ivPlaceholder: ImageView
    private lateinit var textError: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var clearSearchHistoryBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init() //инициализация view
        val sharedPrefers = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefers)
        var historyArray = searchHistory.read() // чтение истории поиска из префс
        val hasNoText = inputEditText.text.isEmpty() //определяем заполнено ли поле поиска
        val hasHistory = historyArray.isNotEmpty()
        val historyAdapter = TrackAdapter()
        //заполнение адаптера для отражения истории поиска
        historyAdapter.trackList = historyArray
        recyclerHistory.adapter = historyAdapter

        fun refreshHistory() {
            historyArray = searchHistory.read()
            historyAdapter.trackList = historyArray
            historyAdapter.notifyDataSetChanged()
        }

        if (savedInstanceState != null) inputEditText.setText(inputTextValue)

        backButton.setOnClickListener {
            finish()
        }

        //Выполнение запроса на поиск треков с кнопки на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeRequest(inputTextValue)
                true
            }
            false
        }

        //Логика отображения истории поиска
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryView.visibility = if (hasFocus && hasNoText && hasHistory) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        //Отображение кнопки очистки поля поиска
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                inputTextValue =  p0.toString()

                refreshHistory()
                searchHistoryView.visibility = if (inputEditText.hasFocus()
                                                    && p0?.isEmpty() == true
                                                    && hasHistory) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
                //TODO("Not yet implemented")
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        //Очистка ввода текста
        clearButton.setOnClickListener {
            clearButton.visibility = View.GONE
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            adapter.trackList.clear()
            clearPlaceholder()
            adapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
        }

        //Отображение результатов поиска
        recycler.layoutManager = LinearLayoutManager(this@SearchActivity)
        recycler.adapter = adapter


        //Обновление результатов поиска, если интернет не подключен
        refreshButton.setOnClickListener {
            clearPlaceholder()
            makeRequest(inputTextValue)
        }

        //Очистка истории поиска
        clearSearchHistoryBtn.setOnClickListener {
            searchHistory.clear()
            searchHistoryView.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, inputTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextValue = savedInstanceState.getString(INPUT_TEXT, DEF_TEXT)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun makeRequest(s: String) {
        searchHistoryView.visibility = View.GONE
        iTunesService.search(s).enqueue(object : Callback<SongsResponse> {
            override fun onResponse(
                call: Call<SongsResponse>,
                response: Response<SongsResponse>
            ) {
                adapter.trackList.clear()
                adapter.trackList = response.body()?.results!!
                adapter.notifyDataSetChanged()
                if (adapter.trackList.isEmpty()) {
                    ivPlaceholder.setImageResource(R.drawable.pic_search_error)
                    textError.setText(R.string.trackNotFound)
                }
            }

            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                ivPlaceholder.setImageResource(R.drawable.pic_network_error)
                textError.setText(R.string.networkError)
                refreshButton.visibility = View.VISIBLE
            }
        })
    }

    private fun clearPlaceholder() {
        ivPlaceholder.setImageResource(0)
        textError.text = ""
        refreshButton.visibility = View.GONE
    }

    private fun init () {
        backButton = findViewById(R.id.imageBackAction)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        recycler = findViewById(R.id.trackView)
        ivPlaceholder = findViewById(R.id.iv_placeholder)
        textError = findViewById(R.id.text_error)
        refreshButton = findViewById(R.id.btn_refresh)
        searchHistoryView = findViewById(R.id.search_history)
        recyclerHistory = findViewById(R.id.rv_track_search_history)
        clearSearchHistoryBtn = findViewById(R.id.btn_clear_search_history)
    }

    companion object {
        const val INPUT_TEXT = "INPUT_TEXT"
        const val DEF_TEXT = ""
    }
}