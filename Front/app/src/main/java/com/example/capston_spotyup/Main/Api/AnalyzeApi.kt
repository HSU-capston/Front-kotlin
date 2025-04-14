package com.example.capston_spotyup.Main.Api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import com.example.capston_spotyup.Main.DTO.Response.AnalyzeResponse
import retrofit2.http.Header

interface AnalyzeApi {
    @Multipart
    @POST("/analyzes/{gameId}")
    fun analyzeVideo(
        @Header("Authorization") token: String,
        @Path("gameId") gameId: Long,
        @Part file: MultipartBody.Part
    ): Call<AnalyzeResponse>
}