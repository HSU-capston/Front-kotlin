package com.example.capston_spotyup.Main.DTO.Response

data class SportsResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val sportsList: List<Sport>
    )

    data class Sport(
        val id: Int,
        val name: String
    )
}