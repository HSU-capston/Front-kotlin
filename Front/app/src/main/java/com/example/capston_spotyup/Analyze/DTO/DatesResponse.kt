package com.example.capston_spotyup.Analyze.DTO

data class DatesResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: GameDateResult?
)

data class GameDateResult(
    val gameDateList: List<String>
)
