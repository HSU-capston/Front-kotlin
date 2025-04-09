package com.example.capston_spotyup.Analyze.Api

import com.example.capston_spotyup.Analyze.DTO.Response.ChartResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChartApi {
    @GET("/games/chart/{sportsId}")
    suspend fun getChartData(
        @Path("sportsId") sportsId: Int,
        @Query("userId") userId: Long
    ): ChartResponse

}
