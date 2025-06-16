package com.niks.googlebooksapp.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.niks.googlebooksapp.Central
import com.niks.googlebooksapp.databinding.BooksItemBinding

class BooksListingAdapter(
    private val layoutInflater: LayoutInflater,
    val itemClick: (bookDetails: BookDetailsVo) -> Unit
) : RecyclerView.Adapter<BookItemViewHolder>() {

    var list = listOf<BookDetailsVo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        val binding = BooksItemBinding.inflate(layoutInflater, parent, false)
        val holder = BookItemViewHolder(binding)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val bookDetails = list[position]
        holder.binding.book = bookDetails
        Central.glideRequestManager.load(bookDetails.imageUrl).into(holder.binding.image)
        holder.binding.bookDetailsRoot.setOnClickListener {
            itemClick(bookDetails)
        }
        holder.binding.actionGetBtn.setOnClickListener {
            itemClick(bookDetails)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemsList(booksList: List<BookDetailsVo>) {
        this.list = booksList
        notifyDataSetChanged()
    }

}

class BookItemViewHolder(val binding: BooksItemBinding) : RecyclerView.ViewHolder(binding.root)