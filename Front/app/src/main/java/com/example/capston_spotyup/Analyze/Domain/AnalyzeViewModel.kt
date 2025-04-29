package com.example.capston_spotyup.Analyze.Domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capston_spotyup.Analyze.DTO.Request.ChartRequest
import com.example.capston_spotyup.Analyze.DTO.Response.ChartResponse
import com.example.capston_spotyup.Analyze.DTO.Response.ChartResult
import com.example.capston_spotyup.Network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.capston_spotyup.Analyze.DTO.Response.DateScore
import com.example.capston_spotyup.Util.TokenManager

class AnalyzeViewModel : ViewModel() {

    private val _chartData = MutableLiveData<ChartResponse>()
    val chartData: LiveData<ChartResponse> get() = _chartData

    fun getChartData(request: ChartRequest) {

        val token = TokenManager.getAccessToken() ?: run {
            Log.e("AnalyzeViewModel", "토큰 없음 - 로그인 필요")
            return
        }
        viewModelScope.launch {
            try {
                // API 호출
                val response = RetrofitClient.chartApi.getChartData(
                    request.sportsId,
                    request.userId,
                    token = "Bearer $token",  // Bearer 앞에 "Bearer "를 붙여야 합니다.
                    request.date,
                )
                _chartData.value = response
            } catch (e: Exception) {
                Log.e("AnalyzeViewModel", "Chart API 실패: ${e.message}")
                _chartData.value = getDummyChartResponse(request.sportsId)  // 실패 시 더미 데이터 반환
            }
        }
    }


    private fun getDummyChartResponse(sportsId: Int): ChartResponse {
        val dummyScores = when (sportsId) {
            1 -> listOf(  // 예시로 볼링 데이터
                DateScore("2025-04-01T00:00:00Z", 180),
                DateScore("2025-04-02T00:00:00Z", 200),
                DateScore("2025-04-03T00:00:00Z", 170),
                DateScore("2025-04-04T00:00:00Z", 210),
                DateScore("2025-04-05T00:00:00Z", 190),
            ).takeLast(5)  // 최근 5개만 가져오기

            2 -> listOf( // 골프
                DateScore("2025-04-01T00:00:00Z", 72),
                DateScore("2025-04-02T00:00:00Z", 68),
                DateScore("2025-04-03T00:00:00Z", 70),
                DateScore("2025-04-04T00:00:00Z", 69),
                DateScore("2025-04-05T00:00:00Z", 71),
            )
            3 -> listOf( // 당구
                DateScore("2025-04-01T00:00:00Z", 100),
                DateScore("2025-04-02T00:00:00Z", 130),
                DateScore("2025-04-03T00:00:00Z", 120),
                DateScore("2025-04-04T00:00:00Z", 140),
                DateScore("2025-04-05T00:00:00Z", 110),
            )
            else -> emptyList()
        }

        val dummyResult = ChartResult(
            gameCount = dummyScores.size,
            averageScore = dummyScores.map { it.gameScore }.average(),
            highScore = dummyScores.maxOfOrNull { it.gameScore } ?: 0,
            lowScore = dummyScores.minOfOrNull { it.gameScore } ?: 0,
            dateScores = dummyScores
        )


        return ChartResponse(
            isSuccess = true,
            code = "DUMMY",
            message = "로컬 더미 데이터",
            result = dummyResult
        )
    }

}
