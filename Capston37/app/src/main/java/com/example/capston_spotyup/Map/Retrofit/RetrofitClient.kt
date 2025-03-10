package com.example.capston_spotyup.Network

import com.example.capston_spotyup.Map.Api.BowlingApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://223.194.134.172:8080" // 🔥 서버 주소 확인!

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(180, TimeUnit.SECONDS) // ⏳ 연결 시간 60초
        .readTimeout(180, TimeUnit.SECONDS) // ⏳ 읽기 시간 60초
        .writeTimeout(180, TimeUnit.SECONDS) // ⏳ 쓰기 시간 60초
        .build()

    val instance: BowlingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 🔥 Timeout 설정 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BowlingApi::class.java)
    }
}
