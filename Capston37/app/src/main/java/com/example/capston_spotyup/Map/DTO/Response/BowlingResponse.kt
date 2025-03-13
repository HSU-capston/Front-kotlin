package com.example.capston_spotyup.Map.DTO.Response

data class BowlingResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultData?
)

data class ResultData(
    val videoUrl: String?
)
