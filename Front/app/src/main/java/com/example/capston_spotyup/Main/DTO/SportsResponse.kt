package com.example.capston_spotyup.Main.DTO

data class SportsResponse(
    val isSuccess :Boolean,
    val code : String,
    val message : String,
    val result : sportsList?

)

data class  sportsList (
    val id : Long,
    val name : String
)
