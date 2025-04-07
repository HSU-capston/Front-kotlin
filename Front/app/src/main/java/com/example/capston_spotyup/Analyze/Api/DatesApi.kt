// DatesApi.kt
package com.example.capston_spotyup.Main.Api


import com.example.capston_spotyup.Analyze.DTO.DatesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DatesApi {
    @GET("/games/dates")
    fun getGameDates(
        @Header("Authorization") token: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<DatesResponse>
}