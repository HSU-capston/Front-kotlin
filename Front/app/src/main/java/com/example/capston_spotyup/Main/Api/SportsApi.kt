package com.example.capston_spotyup.Main.Api


import com.example.capston_spotyup.Main.DTO.Response.SportsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SportsApi {
    @GET("/sports")
    fun getSports(
        @Header("Authorization") token: String
    ): Call<SportsResponse>
}