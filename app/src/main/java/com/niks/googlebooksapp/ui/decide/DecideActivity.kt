package com.niks.googlebooksapp.ui.decide

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.niks.googlebooksapp.compose.ComposeActivity
import com.niks.googlebooksapp.databinding.ActivityDecideBinding
import com.niks.googlebooksapp.ui.list.BooksListingActivity

class DecideActivityBooksDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDecideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDecideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.composeTv.setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }
        binding.dataBindingTv.setOnClickListener {
            startActivity(Intent(this, BooksListingActivity::class.java))
        }
    }
}