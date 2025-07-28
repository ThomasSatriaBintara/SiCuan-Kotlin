package com.example.sicuan

import android.app.Application
import com.example.sicuan.api.ApiClient

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inisialisasi ApiClient dengan context
        ApiClient.init(this)
    }
}