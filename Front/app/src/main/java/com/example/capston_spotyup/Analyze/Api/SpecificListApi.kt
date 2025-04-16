package com.example.capston_spotyup.Analyze.Api

import com.example.capston_spotyup.Analyze.DTO.Response.SpecificListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpecificListApi {
    @GET("/analyzes/{gameId}/list")
    suspend fun getAnalyzeList(
        @Header("Authorization") token: String,
        @Path("gameId") gameId: Long
    ): Response<SpecificListResponse>
}
