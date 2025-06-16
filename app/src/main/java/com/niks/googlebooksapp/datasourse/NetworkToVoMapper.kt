package com.niks.googlebooksapp.datasourse

import com.niks.googlebooksapp.network.entities.BookItem
import com.niks.googlebooksapp.ui.list.BookDetailsVo

fun BookItem.toVo(): BookDetailsVo {
    return BookDetailsVo(
        id = id,
        title = volumeInfo.title,
        publisher = volumeInfo.publisher,
        averageRating = volumeInfo.averageRating,
        pageCount = volumeInfo.pageCount,
        imageUrl = volumeInfo.imageLinks.smallThumbnail
    )
}

fun List<BookItem>.mapToVo(): List<BookDetailsVo> {
    return this.map { it.toVo() }
}