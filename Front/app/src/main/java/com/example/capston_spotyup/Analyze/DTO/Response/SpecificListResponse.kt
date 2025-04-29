package com.example.capston_spotyup.Analyze.DTO.Response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


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

@Parcelize
data class AnalyzeListItem(
    val id: Long
) : Parcelable
