package com.niks.googlebooksapp

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.niks.googlebooksapp.network.AppNetworkApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// This is like app level singleton this we required in app
object Central {

    lateinit var applicationContext: Context

    val glideRequestManager: RequestManager by lazy {
        Glide.with(applicationContext)
    }
    val gson: Gson by lazy { Gson() }

    private val retrofit: Retrofit by lazy {
        //TODO - Pick base url from build config of gradle
        Retrofit.Builder().baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val appNetworkApi: AppNetworkApi by lazy {
        retrofit.create(AppNetworkApi::class.java)
    }

}