package com.example.capston_spotyup.Signup.Api

import SmsVerificationRequest
import SmsVerificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SmsVerificationApi {
    @POST("/auth/sms/verification")
    fun verifySmsCode(@Body request:SmsVerificationRequest): Call<SmsVerificationResponse>
}