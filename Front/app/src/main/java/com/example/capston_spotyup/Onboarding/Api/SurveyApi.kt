package com.example.capston_spotyup.Survey.Api

import com.example.capston_spotyup.Onboarding.DTO.Request.SurveyRequest
import com.example.capston_spotyup.Onboarding.DTO.Response.SurveyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SurveyApi {
    @POST("/users/survey")
    suspend fun submitSurvey(
        @Header("Authorization") token: String,
        @Body surveyRequest: SurveyRequest
    ): Response<SurveyResponse>
}
