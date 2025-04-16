package com.example.capston_spotyup.Analyze.DTO.Response

data class SpecificListResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AnalyzeListResult
)

data class AnalyzeListResult(
    val listSize: Int,
    val analyzeList: List<AnalyzeListItem>
)

data class AnalyzeListItem(
    val id: Long
)
