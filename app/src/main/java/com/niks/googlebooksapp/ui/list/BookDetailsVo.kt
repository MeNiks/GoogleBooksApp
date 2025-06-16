package com.niks.googlebooksapp.ui.list

import android.os.Parcelable
import com.niks.googlebooksapp.Central
import com.niks.googlebooksapp.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookDetailsVo(
    val id:String,
    val title:String,
    val publisher:String,
    val averageRating:Float,
    val pageCount:Int,
    val imageUrl:String
) : Parcelable {
    fun getRating(): String {
        return "$averageRating"
    }

    fun getPageCount(): String {
        return "($pageCount)"
    }

    fun getPageCountForDetails(): String {
        return "$pageCount Pages"
    }


}

