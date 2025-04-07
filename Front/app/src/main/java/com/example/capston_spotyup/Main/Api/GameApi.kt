package com.example.capston_spotyup.Main.Api

import com.example.capston_spotyup.Main.DTO.Request.GameRequest
import com.example.capston_spotyup.Main.DTO.Response.GameResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GameApi {
    @POST("/games/manual")
    fun startManualGame(
        @Header("Authorization") token: String,
        @Body request: GameRequest
    ): Call<GameResponse>
}