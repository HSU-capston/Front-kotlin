package com.example.capston_spotyup.Login.DTO

data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: LoginResult?
)

data class LoginResult(
    val accessToken: String,
    val refreshToken: String
)
