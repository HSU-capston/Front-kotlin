package com.example.capston_spotyup.Analyze.DTO.Response

data class ChartResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ChartResult
)

data class ChartResult(
    val gameCount: Int,
    val averageScore: Double,
    val highScore: Int,
    val lowScore: Int,
    val dateScores: List<DateScore>
)

data class DateScore(
    val gameDate: String,  // ISO 형식 "2025-04-02T05:46:18.189Z"
    val gameScore: Int
)

data class GameInfo(
    val id: Int,            // 게임 ID
    val sportsId: Int,      // 스포츠 ID
    val playDate: String,   // 게임 날짜
    val score: Int          // 게임 점수
)


