package com.niks.googlebooksapp.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.niks.googlebooksapp.R
import com.niks.googlebooksapp.databinding.ActivityBooksListingBinding
import com.niks.googlebooksapp.ui.details.BooksDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BooksListingActivity : AppCompatActivity() {
    lateinit var binding: ActivityBooksListingBinding

    private val viewModel: BooksListingActivityViewModel by viewModels()
    private lateinit var adapter: BooksListingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBooksListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.listenUiState()
                .collectLatest { uiState ->
                    withContext(Dispatchers.Main) {
                        handleViewState(uiState)
                    }
                }
        }
    }

    private fun handleViewState(uiState: BooksListingUIState) {
        if (uiState.isLoading) {
            binding.progressBar.isVisible = true
            binding.recyclerView.isVisible = false
            binding.errorMessageTv.isVisible = false
        } else if (uiState.isError) {
            binding.errorMessageTv.isVisible = true
            binding.errorMessageTv.text = uiState.errorMessage
            binding.progressBar.isVisible = false
            binding.recyclerView.isVisible = false
        } else {
            binding.recyclerView.isVisible = true
            binding.progressBar.isVisible = false
            binding.errorMessageTv.isVisible = false
            adapter.setItemsList(uiState.booksList)
        }
    }

    private fun setupRecyclerView() {
        adapter = BooksListingAdapter(layoutInflater) { bookDetails ->
            startActivity(
                Intent(this, BooksDetailsActivity::class.java).apply {
                    putExtra(BooksDetailsActivity.EXTRA_BOOK, bookDetails)
                }
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

}





