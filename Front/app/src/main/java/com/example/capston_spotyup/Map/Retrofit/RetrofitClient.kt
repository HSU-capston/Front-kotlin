package com.example.capston_spotyup.Network

import ListApi
import com.example.capston_spotyup.Analyze.Api.ChartApi
import com.example.capston_spotyup.Analyze.Api.HighlightAPi
import com.example.capston_spotyup.Analyze.Api.SpecificApi
import com.example.capston_spotyup.Analyze.Api.SpecificListApi
import com.example.capston_spotyup.Login.Api.LoginApi
import com.example.capston_spotyup.Main.Api.AnalyzeApi
import com.example.capston_spotyup.Main.Api.DatesApi
import com.example.capston_spotyup.Main.Api.GameApi
import com.example.capston_spotyup.Main.Api.GameExitApi
import com.example.capston_spotyup.Main.Api.SportsApi
import com.example.capston_spotyup.Map.Api.BowlingApi
import com.example.capston_spotyup.Profile.Api.FriendApi
import com.example.capston_spotyup.Profile.Api.UserApi
import com.example.capston_spotyup.Signup.Api.SmsVerificationApi
import com.example.capston_spotyup.User.Api.EmailApi
import com.example.capston_spotyup.Home.Api.HomeApi
import com.example.capston_spotyup.Survey.Api.SurveyApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://13.209.69.164:8080" //

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS) // ⏳ 연결 시간 60초
        .readTimeout(300, TimeUnit.SECONDS) // ⏳ 읽기 시간 60초
        .writeTimeout(300, TimeUnit.SECONDS) // ⏳ 쓰기 시간 60초
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
    val listApi: ListApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListApi::class.java)
    }
    val userApi: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    val gameExitApi: GameExitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameExitApi::class.java)
    }

    val chartApi: ChartApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChartApi::class.java)
    }

    val analyzeApi: AnalyzeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnalyzeApi::class.java)
    }

    val homeApi: HomeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HomeApi::class.java)
    }

    val specificApi: SpecificApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpecificApi::class.java)
    }

    val specificListApi: SpecificListApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpecificListApi::class.java)
    }


    val friendApi: FriendApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FriendApi::class.java)  // friendApi 인터페이스를 만들어야 합니다.
    }

    val surveyApi: SurveyApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SurveyApi::class.java)
    }

    val highlightAPi: HighlightAPi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HighlightAPi::class.java)
    }

}
