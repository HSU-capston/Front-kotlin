package com.example.capston_spotyup.Analyze.Api

import com.example.capston_spotyup.Analyze.DTO.Response.SpecificAnalyzeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpecificApi {
    @GET("/analyzes/{analyzeId}")
    suspend fun getSpecificAnalyze(
        @Header("Authorization") token: String,
        @Path("analyzeId") analyzeId: Long
    ): Response<SpecificAnalyzeResponse>
}
