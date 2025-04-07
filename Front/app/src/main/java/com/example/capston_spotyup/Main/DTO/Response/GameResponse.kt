package com.example.capston_spotyup.Main.DTO.Response

data class GameResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val id: Int
    )
}