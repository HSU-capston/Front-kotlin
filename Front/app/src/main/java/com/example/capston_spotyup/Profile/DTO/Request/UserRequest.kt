package com.example.capston_spotyup.Profile.DTO.Request


data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone_num: String
)