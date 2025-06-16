package com.niks.googlebooksapp.network

import com.niks.googlebooksapp.network.entities.BooksListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppNetworkApi {

    @GET("/books/v1/volumes")
    suspend fun getBooks(
        @Query("q")
        query: String = "jkrowling"
    ): BooksListResponse
}