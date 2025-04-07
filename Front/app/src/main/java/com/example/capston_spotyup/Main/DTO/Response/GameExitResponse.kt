package com.example.capston_spotyup.Main.DTO.Response

data class GameExitResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: GameExitResult
)

data class GameExitResult(
    val id: Long,
    val summary: String,
    val highlightUrl: String,
    val score: Int
)
