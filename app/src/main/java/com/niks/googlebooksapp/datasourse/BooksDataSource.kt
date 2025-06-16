package com.niks.googlebooksapp.datasourse

import com.niks.googlebooksapp.ui.list.BookDetailsVo

interface BooksDataSource {

    suspend fun getBooks():List<BookDetailsVo>
}