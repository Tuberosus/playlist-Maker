package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.SearchScreenState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
    }

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private var inputTextValue = DEF_TEXT
    private var isClickAllowed = true //определение состояния клика для debounce
    private var historyArray: ArrayList<Track>? = null

//    private val baseUrl = "https://itunes.apple.com"

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val handler = Handler(Looper.getMainLooper())
//    private val searchRunnable = Runnable {makeRequest(inputTextValue)}

//    private lateinit var backButton: ImageView
//    private lateinit var inputEditText: EditText
//    private lateinit var clearButton: ImageView
//    private lateinit var recycler: RecyclerView
//    private lateinit var ivPlaceholder: ImageView
//    private lateinit var textError: TextView
//    private lateinit var refreshButton: Button
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var clearSearchHistoryBtn: Button
//    private lateinit var search_progressbar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init() //инициализация view
//        val sharedPrefers = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        //val searchHistoryImpl = SearchHistoryImpl(application)
        //var historyArray = searchHistoryImpl.read() // чтение истории поиска из префс
        val hasNoText = binding.inputEditText.text.isEmpty() //определяем заполнено ли поле поиска
        val hasHistory = historyArray?.isNotEmpty() ?: true

        viewModel.getState().observe(this) {state ->
            when(state) {
                SearchScreenState.Loading -> { loadingSearch() }
                is SearchScreenState.Content -> { showTracks(state.trackList) }
                is SearchScreenState.Empty -> { showEmptyError() }
                is SearchScreenState.Error -> { showError() }
                is SearchScreenState.SearchHistoryContent -> {
                    historyArray = state.trackList
                    showHistory(historyArray) }

                else -> {}
            }
        }

        viewModel.getTrackClickEvent().observe(this) {track ->
            openPlayer(track)
        }

        adapter = TrackAdapter {
//            openPlayer(it)
            viewModel.onTrackClick(it)
        }
        historyAdapter = TrackAdapter {
            //openPlayer(it)
            viewModel.onTrackClick(it)
        }
        //заполнение адаптера для отражения истории поиска
//        historyAdapter.trackList = historyArray
//        recyclerHistory.adapter = historyAdapter

        fun refreshHistory() {
//            historyArray = searchHistoryImpl.read()
//            historyAdapter.trackList = historyArray
//            historyAdapter.notifyDataSetChanged()
        }

        if (savedInstanceState != null) binding.inputEditText.setText(inputTextValue)

        binding.imageBackAction.setOnClickListener {
            finish()
        }

        //Выполнение запроса на поиск треков с кнопки на клавиатуре
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //makeRequest(inputTextValue)
                viewModel.loadData(inputTextValue)
                true
            }
            false
        }

        //Логика отображения истории поиска
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
//            searchHistoryView.visibility = if (hasFocus && hasNoText && hasHistory) {
//                View.VISIBLE
//            } else {
//                View.GONE
//            }
            viewModel.showHistory()
        }

        //Отображение кнопки очистки поля поиска
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(p0)
                inputTextValue =  p0.toString()

                if (inputTextValue.isNotEmpty()) {
//                    searchDebounce()
                    viewModel.searchDebounce(
                        changedText = inputTextValue
                    )
                    adapter.trackList.clear()
                } else {
                    adapter.trackList.clear()
                    adapter.notifyDataSetChanged()
                }

//                refreshHistory()
                searchHistoryView.visibility = if (binding.inputEditText.hasFocus()
                    && inputTextValue.isEmpty()
                    && hasHistory) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                //TODO("Not yet implemented")
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        //Очистка ввода текста
        binding.clearIcon.setOnClickListener {
            binding.clearIcon.visibility = View.GONE
            binding.inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            adapter.trackList.clear()
            clearPlaceholder()
            adapter.notifyDataSetChanged()
            showHistory(historyArray)
        }

        //Отображение результатов поиска
//        recycler.layoutManager = LinearLayoutManager(this@SearchActivity)
//        recycler.adapter = adapter


        //Обновление результатов поиска, если интернет не подключен
        binding.btnRefresh.setOnClickListener {
            clearPlaceholder()
//            makeRequest(inputTextValue)
            viewModel.loadData(inputTextValue)
        }

        //Очистка истории поиска
        clearSearchHistoryBtn.setOnClickListener {
            viewModel.clearSearchHistory()
//            searchHistoryImpl.clear()
//            searchHistoryView.visibility = View.GONE
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(INPUT_TEXT, inputTextValue)
//    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        inputTextValue = savedInstanceState.getString(INPUT_TEXT, DEF_TEXT)
//    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun loadingSearch() {
        clearPlaceholder()
        hideHistory()
        binding.searchProgressbar.visibility = View.VISIBLE
    }

//    private fun makeRequest(s: String) {
//        if (s.isEmpty()) return
//        searchHistoryView.visibility = View.GONE
//        search_progressbar.visibility = View.VISIBLE
//        adapter.trackList.clear()
//        clearPlaceholder()
//
//        iTunesService.search(s).enqueue(object : Callback<SongsResponse> {
//            override fun onResponse(
//                call: Call<SongsResponse>,
//                response: Response<SongsResponse>
//            ) {
//                search_progressbar.visibility= View.GONE
//                adapter.trackList = response.body()?.results!!
//                adapter.notifyDataSetChanged()
//                if (adapter.trackList.isEmpty() && s.isNotEmpty()) {
//                    ivPlaceholder.setImageResource(R.drawable.pic_search_error)
//                    textError.setText(R.string.trackNotFound)
//                }
//            }
//
//            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
//                search_progressbar.visibility = View.GONE
//                ivPlaceholder.setImageResource(R.drawable.pic_network_error)
//                textError.setText(R.string.networkError)
//                refreshButton.visibility = View.VISIBLE
//            }
//        })
//
//    }

    private fun showHistory(trackList: ArrayList<Track>?) {
        recyclerHistory.adapter = historyAdapter
        if (trackList.isNullOrEmpty()) {
            historyAdapter.trackList.clear()
            searchHistoryView.visibility = View.GONE
        } else {
            historyAdapter.trackList = trackList
            searchHistoryView.visibility = View.VISIBLE
        }
        historyAdapter.notifyDataSetChanged()
    }

    private fun hideHistory() {
        searchHistoryView.visibility = View.GONE
    }

    private fun showTracks(trackList: ArrayList<Track>) {
        adapter.trackList.clear()
        binding.searchProgressbar.visibility= View.GONE
        adapter.trackList = trackList
        binding.trackView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyError() {
        binding.searchProgressbar.visibility = View.GONE
        searchHistoryView.visibility = View.GONE
        binding.ivPlaceholder.setImageResource(R.drawable.pic_search_error)
        binding.textError.setText(R.string.trackNotFound)
    }

    private fun showError() {
        binding.searchProgressbar.visibility = View.GONE
        searchHistoryView.visibility = View.GONE
        binding.ivPlaceholder.setImageResource(R.drawable.pic_network_error)
        binding.textError.setText(R.string.networkError)
        binding.btnRefresh.visibility = View.VISIBLE
    }

    private fun clearPlaceholder() {
        binding.ivPlaceholder.setImageResource(0)
        binding.textError.text = ""
        binding.btnRefresh.visibility = View.GONE
    }

//    private fun searchDebounce() {
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
//    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    //функция для передачи в адаптер, для открытия плеера
    private fun openPlayer(jsonTrack: String) {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(AudioPlayerActivity.TRACK_TAG, jsonTrack)
            startActivity(intent)
            Log.d("MyTag", jsonTrack.toString())
        }
    }

    private fun init () {
//        backButton = findViewById(R.id.imageBackAction)
//        inputEditText = findViewById(R.id.inputEditText)
//        clearButton = findViewById(R.id.clearIcon)
//        recycler = findViewById(R.id.trackView)
//        ivPlaceholder = findViewById(R.id.iv_placeholder)
//        textError = findViewById(R.id.text_error)
//        refreshButton = findViewById(R.id.btn_refresh)
        searchHistoryView = findViewById(R.id.search_history)
        recyclerHistory = findViewById(R.id.rv_track_search_history)
        clearSearchHistoryBtn = findViewById(R.id.btn_clear_search_history)
//        search_progressbar = findViewById((R.id.search_progressbar))
    }

    companion object {
        const val INPUT_TEXT = "INPUT_TEXT"
        const val DEF_TEXT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}