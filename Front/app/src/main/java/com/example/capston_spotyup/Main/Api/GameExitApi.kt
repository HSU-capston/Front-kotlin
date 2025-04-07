package com.example.capston_spotyup.Main.Api

import com.example.capston_spotyup.Main.DTO.Request.GameExitRequest
import com.example.capston_spotyup.Main.DTO.Response.GameExitResponse
import retrofit2.Call
import retrofit2.http.*

interface GameExitApi {
    @PATCH("/games/{gameId}")
    fun exitGame(
        @Header("Authorization") token: String,
        @Path("gameId") gameId: Long,
        @Body request: GameExitRequest
    ): Call<GameExitResponse>
}
