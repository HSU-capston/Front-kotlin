package com.example.capston_spotyup.Main.Api


import com.example.capston_spotyup.Main.DTO.SportsResponse
import com.example.capston_spotyup.data.model.ChartResponse
import retrofit2.Call
import retrofit2.http.GET

interface ChartApi {
    @GET("/Chart")
    fun registerUser(): Call<ChartResponse>
}

