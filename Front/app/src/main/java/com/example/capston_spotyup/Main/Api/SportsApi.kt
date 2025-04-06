package com.example.capston_spotyup.Main.Api


import EmailRequest
import com.example.capston_spotyup.Main.DTO.SportsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface SportsApi {
    @GET("/sports")
    fun registerUser(): Call<SportsResponse>
}
