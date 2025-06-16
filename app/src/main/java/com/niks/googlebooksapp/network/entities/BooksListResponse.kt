package com.niks.googlebooksapp.network.entities

data class BooksListResponse(
    val totalItems: Int,
    val items: List<BookItem>
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val publisher: String,
    val pageCount: Int,
    val averageRating: Float,
    val imageLinks:ImageLinks
)

data class ImageLinks(
    val thumbnail:String,
    val smallThumbnail:String
)