package com.example.capston_spotyup

import android.content.Context
import android.util.Log
import com.example.capston_spotyup.Util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // TokenManager를 통해 직접 토큰을 가져옵니다.
        val token = TokenManager.getAccessToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
