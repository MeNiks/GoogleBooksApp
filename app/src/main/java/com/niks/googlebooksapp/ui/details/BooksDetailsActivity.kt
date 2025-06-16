package com.niks.googlebooksapp.ui.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.niks.googlebooksapp.Central
import com.niks.googlebooksapp.databinding.ActivityBookDetailsBinding
import com.niks.googlebooksapp.ui.list.BookDetailsVo

class BooksDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        renderUI()
    }

    private fun renderUI() {
        val bookDetailsVo = intent.getParcelableExtra(EXTRA_BOOK) as? BookDetailsVo ?: return
        binding.book = bookDetailsVo
        Central.glideRequestManager.load(bookDetailsVo.imageUrl).into(binding.imageView)
        binding.crossBtn.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_BOOK = "book"
    }
}