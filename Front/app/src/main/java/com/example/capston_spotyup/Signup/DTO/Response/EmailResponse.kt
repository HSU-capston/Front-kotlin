package com.example.capston_spotyup.User.DTO

data class EmailResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: TokenResult?
)

data class TokenResult(
    val accessToken: String,
    val refreshToken: String
)
