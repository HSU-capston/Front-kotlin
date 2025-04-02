package com.example.capston_spotyup.data.model

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
    val gameDate: String,
    val gameScore: Int
)