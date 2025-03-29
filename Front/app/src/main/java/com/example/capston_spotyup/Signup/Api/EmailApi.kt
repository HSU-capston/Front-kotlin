package com.example.capston_spotyup.User.Api


import EmailRequest
import EmailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EmailApi {
    @Headers("Content-Type: application/json")
    @POST("/users/email")
    fun registerUser(@Body request: EmailRequest): Call<EmailResponse>
}
