package com.example.capston_spotyup.Profile.DTO.Request


data class UserRequest(
    val name: String,
    val nickname: String,  // 👈 요청용도에도 있어야 함
    val email: String,
    val password: String,
    val phone_num: String
)