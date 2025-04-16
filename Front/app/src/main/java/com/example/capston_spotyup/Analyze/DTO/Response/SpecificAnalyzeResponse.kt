package com.example.capston_spotyup.Analyze.DTO.Response

data class SpecificAnalyzeResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AnalyzeDetailResult
)

data class AnalyzeDetailResult(
    val id: Long,
    val videoUrl: String,
    val poseScore: String,
    val recommendPose: String,
    val feedback: String? // 선택 사항
)
