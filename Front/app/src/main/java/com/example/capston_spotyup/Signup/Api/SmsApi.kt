package com.example.capston_spotyup.Network

import SmsRequest
import SmsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SmsApi {
    @POST("/auth/sms/send")
    fun sendSms(@Body request: SmsRequest): Call<SmsResponse>
}
