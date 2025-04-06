package com.example.capston_spotyup.Main

import android.app.Application
import com.example.capston_spotyup.Util.TokenManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}
