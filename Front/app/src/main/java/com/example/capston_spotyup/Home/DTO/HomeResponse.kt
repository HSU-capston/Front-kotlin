package com.example.capston_spotyup.Home.DTO
data class HomeResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultData
)

data class ResultData(
    val listSize: Int,
    val recommendedVideoList: List<RecommendedVideo>
)

data class RecommendedVideo(
    val videoUrl: String,
    val thumbnailUrl: String,
    val title : String,
    val channelTitle : String
)
