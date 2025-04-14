package com.example.capston_spotyup.Onboarding.DTO.Request

data class SurveyRequest(
    val sportsId: Int,
    val usageReason: String,
    val level: String,     // 예: BEGINNER, INTERMEDIATE, ADVANCED
    val goal: String       // 예: GENERAL, COMPETITION, HEALTH, etc.
)
