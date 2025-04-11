package com.example.capston_spotyup.Main

import android.app.Application
import com.example.capston_spotyup.Util.TokenManager
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        KakaoSdk.init(this, "<>")
    }
}
