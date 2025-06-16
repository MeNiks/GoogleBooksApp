package com.niks.googlebooksapp.datasourse

import com.niks.googlebooksapp.Central
import com.niks.googlebooksapp.network.AppNetworkApi
import com.niks.googlebooksapp.ui.list.BookDetailsVo

class BooksRemoteDataSource
constructor(
    //TODO - Inject this from di
    private val appNetworkApi: AppNetworkApi = Central.appNetworkApi
) : BooksDataSource {

    override suspend fun getBooks(): List<BookDetailsVo> {
        val booksResponse = appNetworkApi.getBooks()
        return booksResponse.items.mapToVo()
    }
}