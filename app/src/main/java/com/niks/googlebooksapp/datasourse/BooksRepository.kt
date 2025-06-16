package com.niks.googlebooksapp.datasourse

import com.niks.googlebooksapp.ui.list.BookDetailsVo

class BooksRepository(
    //TODO - Inject this by di
    private val booksRemoteDataSource: BooksRemoteDataSource = BooksRemoteDataSource()
) :BooksDataSource{

    override suspend fun getBooks(): List<BookDetailsVo> {
        return booksRemoteDataSource.getBooks()
    }
}