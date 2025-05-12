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
    val goodPoint :String,
    val badPoint:String,
    val feedback: String?,
    val score: Int,
    val shoulderAngleDiff :Int,
    val movementDistance : Int,
    val wristMovementTotal: Int,
    val ankleSwitchCount : Int
)
