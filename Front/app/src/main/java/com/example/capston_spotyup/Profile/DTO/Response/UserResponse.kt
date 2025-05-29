package com.example.capston_spotyup.Profile.DTO.Response

data class UserResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: UserResult
)

data class UserResult(
    val name: String,
    val email: String,
    val password: String,
    val phoneNum: String
)

data class Friend(
    val id: Long,
    val name: String,
    val profileImageUrl: String
)
