package com.example.capston_spotyup.Analyze.DTO.Request

data class ChartRequest(
    val userId: Long,    // 사용자 ID
    val sportsId: Int,   // 스포츠 ID
    val date: String,    // 날짜 (예: "2025-04-30")
    val token: String    // Authorization 토큰
)