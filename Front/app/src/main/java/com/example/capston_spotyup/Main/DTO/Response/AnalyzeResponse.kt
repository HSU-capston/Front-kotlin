package com.example.capston_spotyup.Main.DTO.Response

data class AnalyzeResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AnalyzeResult
)

data class AnalyzeResult(
    val id: Long,
    val poseScore: String,
    val recommendPose: String,
    val videoUrl: String
)
