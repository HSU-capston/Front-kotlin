package com.example.capston_spotyup.Login.Api

import com.example.capston_spotyup.Login.DTO.LoginRequest
import com.example.capston_spotyup.Login.DTO.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/login/email")
    fun loginWithEmail(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}
