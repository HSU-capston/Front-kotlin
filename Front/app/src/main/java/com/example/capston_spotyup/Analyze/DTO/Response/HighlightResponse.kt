package com.example.capston_spotyup.Analyze.DTO.Response



data class HighlightResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result  : successhighlisht
)

data class successhighlisht(
    val id : Long,
    val summary : String,
    val score : Long,
    val highlightUrl : String
)
