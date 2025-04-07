package com.example.capston_spotyup.Network

import com.example.capston_spotyup.Login.Api.LoginApi
import com.example.capston_spotyup.Main.Api.DatesApi
import com.example.capston_spotyup.Main.Api.GameApi
import com.example.capston_spotyup.Main.Api.GameExitApi
import com.example.capston_spotyup.Main.Api.SportsApi
import com.example.capston_spotyup.Map.Api.BowlingApi
import com.example.capston_spotyup.Signup.Api.SmsVerificationApi
import com.example.capston_spotyup.User.Api.EmailApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://13.209.69.164:8080" // 🔥 서버 주소 확인!

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(180, TimeUnit.SECONDS) // ⏳ 연결 시간 60초
        .readTimeout(180, TimeUnit.SECONDS) // ⏳ 읽기 시간 60초
        .writeTimeout(180, TimeUnit.SECONDS) // ⏳ 쓰기 시간 60초
        .build()
    // Timeout 에따라 연결

    val instance: BowlingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 🔥 Timeout 설정 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BowlingApi::class.java)
    }
    val emailApi: EmailApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmailApi::class.java)
    }

    val sportsApi: SportsApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SportsApi::class.java)
    }
    val datesApi: DatesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DatesApi::class.java)
    }
    // smsApi 추가
    val smsApi: SmsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmsApi::class.java)
    }

    val smsVerificationApi: SmsVerificationApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmsVerificationApi::class.java)
    }
    val loginApi: LoginApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApi::class.java)
    }

    val gameApi: GameApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApi::class.java)
    }

    val gameExitApi: GameExitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameExitApi::class.java)
    }






}
