package com.example.capston_spotyup.Map.Api

import com.example.capston_spotyup.Map.DTO.Response.BowlingResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BowlingApi {

    @Multipart
    @POST("/bowling/analyze") // 🔥 서버 엔드포인트 확인 필요
    fun analyzeBowling(
        @Part file: MultipartBody.Part // 🔥 "file" 필드 확인!
    ): Call<BowlingResponse> // 응답 타입은 BowlingResponse
}
