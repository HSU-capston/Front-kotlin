package com.example.app

import GameListResponse
import ListApi
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Util.TokenManager


import com.example.capston_spotyup.databinding.FragmentAnalyzeSubBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeFragmentSub : Fragment() {

    private var _binding: FragmentAnalyzeSubBinding? = null
    private val binding get() = _binding!!

    private lateinit var analyzeAdapter: AnalyzeSubAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalyzeSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        analyzeAdapter = AnalyzeSubAdapter()
        binding.recycle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = analyzeAdapter
        }

        // arguments로 전달된 selectedDate 받기, AnalyzeFragmentCalendar에서 받은 값
        val selectedDate = arguments?.getString("selectedDate") ?: "2025-04-08" // 기본값 설정
        Log.d("AnalyzeFragmentSub", "선택된 날짜: $selectedDate")

        fetchGameList(selectedDate) // API 호출하여 데이터 가져오기

    }
    // 게임 목록을 가져오는 함수
    private fun fetchGameList(date: String) {
        val token = TokenManager.getAccessToken()  // TokenManager에서 토큰을 가져옵니다.
        if (token == null) {
            Log.e("GameListAPI", "토큰 없음 - 로그인 필요")
            return
        }
        Log.d("GameListAPI", "사용 중인 토큰: $token") // 로그로 토큰 확인

        // API 호출

        RetrofitClient.listApi.getGameList("Bearer $token", date)
            .enqueue(object : Callback<GameListResponse> {
                override fun onResponse(
                    call: Call<GameListResponse>, response: Response<GameListResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.result?.gameInfoList?.let { gameList ->

                            gameList.forEach { game ->
                                Log.d("GameListAPI", "게임 정보: ${game.id}, ${game.sportsId}, ${game.playDate}, ${game.score}")
                            }
                            // 데이터를 RecyclerView에 업데이트
                            val analyzeItems = gameList.map {
                                AnalyzeItem(it.playDate, it.score.toString()) // 날짜와 점수를 맞추어 데이터 변환
                            }
                            analyzeAdapter.submitList(analyzeItems)  // 어댑터에 데이터 설정
                        }
                    } else {
                        Log.e("GameListAPI", "응답 실패: ${response.code()} - ${response.message()}")
                        // 응답 실패 시 403인 경우 토큰 만료 확인
                        if (response.code() == 403) {
                            Log.e("GameListAPI", "403 Forbidden - 토큰 만료될 수 있음")
                            // refreshtoken 쓴다고 해결될게 아닌거 같은데
                        }
                    }
                }

                override fun onFailure(call: Call<GameListResponse>, t: Throwable) {
                    Log.e("GameListAPI", "API 호출 실패", t)
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
