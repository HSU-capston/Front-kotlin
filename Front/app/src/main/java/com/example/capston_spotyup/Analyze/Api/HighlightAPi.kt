package com.example.capston_spotyup.Analyze.Api

import com.example.capston_spotyup.Analyze.DTO.Response.HighlightResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HighlightAPi {
    @GET("/games/{gameId}")
    fun hightlightservice(
        @Header("Authorization") token: String,
        @Path ("gameId") gameId: Long
    ): Call<HighlightResponse>
}