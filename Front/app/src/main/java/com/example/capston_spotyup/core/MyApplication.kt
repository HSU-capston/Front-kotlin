package com.example.capston_spotyup.core

import android.app.Application
import android.util.Log
import com.example.capston_spotyup.BuildConfig
import com.example.capston_spotyup.Util.TokenManager
import com.kakao.sdk.common.KakaoSdk


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
//        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        Log.d("KakaoAppKey", "App Key: ${BuildConfig.KAKAO_APP_KEY}")
    }
}
