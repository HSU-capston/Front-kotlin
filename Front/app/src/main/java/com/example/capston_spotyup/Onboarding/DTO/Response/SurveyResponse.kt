package com.example.capston_spotyup.Onboarding.DTO.Response

data class SurveyResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any // 필요 시 result 타입 정의 가능
)