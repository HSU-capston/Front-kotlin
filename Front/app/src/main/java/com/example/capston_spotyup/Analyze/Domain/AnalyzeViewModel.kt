package com.example.capston_spotyup.Analyze.Domain

import ChartResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capston_spotyup.Network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeViewModel : ViewModel() {

    private val _chartData = MutableLiveData<ChartResponse>()
    val chartData: LiveData<ChartResponse> get() = _chartData

    // 특정 스포츠의 차트 데이터를 가져오는 함수
    suspend fun getChartData(userId: Long, sportsId: Int) {
        val call = RetrofitClient.ChartApi.getChartData(userId, sportsId)
        call.enqueue(object : Callback<ChartResponse> {
            override fun onResponse(call: Call<ChartResponse>, response: Response<ChartResponse>) {
                if (response.isSuccessful) {
                    _chartData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}
