package com.example.capston_spotyup.Analyze.DTO

data class ChartResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ChartResult
)

data class ChartResult(
    val gameCount: Int,
    val averageScore: Int,
    val highScore: Int,
    val lowScore: Int,
    val dateScores: List<DateScore>
)

data class DateScore(
    val gameDate: String,  // ISO 형식 "2025-04-02T05:46:18.189Z"
    val gameScore: Int
)

