package com.example.capston_spotyup.Home.Api

import com.example.capston_spotyup.Home.DTO.HomeResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Response

interface HomeApi {
    @GET("/home/recommendations")
    suspend fun getHomeRecommendations(
        @Header("Authorization") token: String
    ): Response<HomeResponse>
}
