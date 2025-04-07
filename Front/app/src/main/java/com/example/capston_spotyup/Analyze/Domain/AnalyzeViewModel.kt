package com.example.capston_spotyup.Analyze.Domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capston_spotyup.Analyze.DTO.ChartResponse
import com.example.capston_spotyup.Network.RetrofitClient
import kotlinx.coroutines.launch

class AnalyzeViewModel : ViewModel() {

    private val _chartData = MutableLiveData<ChartResponse>()
    val chartData: LiveData<ChartResponse> get() = _chartData

    fun getChartData(userId: Long, sportsId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.chartApi.getChartData(sportsId, userId)
                _chartData.value = response
            } catch (e: Exception) {
                Log.e("AnalyzeViewModel", "Chart API 실패: ${e.message}")
            }
        }
    }
}
