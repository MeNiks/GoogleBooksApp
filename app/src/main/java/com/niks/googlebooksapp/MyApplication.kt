package com.niks.googlebooksapp

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Central.applicationContext = this
    }
}