package com.example.capston_spotyup.Profile.Api

import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.DTO.Response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserApi {
    @GET("/users")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PATCH("/users")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Body userData: UserRequest
    ): Response<UserResponse>
}
