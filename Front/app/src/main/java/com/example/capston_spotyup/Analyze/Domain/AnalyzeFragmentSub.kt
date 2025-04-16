package com.example.app

import GameListResponse
import ListApi
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capston_spotyup.Analyze.Domain.SpecificFragment
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Util.TokenManager


import com.example.capston_spotyup.databinding.FragmentAnalyzeSubBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

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

        analyzeAdapter = AnalyzeSubAdapter { gameId ->
            onAllVideoClicked(gameId) //
        }

        binding.recycle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = analyzeAdapter
        }

        val selectedDate = arguments?.getString("selectedDate") ?: "2025-04-08"
        Log.d("AnalyzeFragmentSub", "선택된 날짜: $selectedDate")
        fetchGameList(selectedDate)
    }
//    private fun onAllVideoClicked(gameId: Int) {
//        Toast.makeText(requireContext(), "전체영상 클릭됨: $gameId", Toast.LENGTH_SHORT).show()
//        val token = TokenManager.getAccessToken() ?: run {
//            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        lifecycleScope.launch {
//            try {
//                Log.d("AllVideoAPI", "API 요청 시작 - gameId: $gameId")
//
//                val response = RetrofitClient.specificListApi.getAnalyzeList("Bearer $token", gameId.toLong())
//                Log.d("AllVideoAPI", "응답 수신됨 - isSuccessful: ${response.isSuccessful}")
//
//                if (response.isSuccessful && response.body()?.isSuccess == true) {
//                    val analyzeId = response.body()?.result?.analyzeList?.firstOrNull()?.id
//                    if (analyzeId != null) {
//                        val bundle = Bundle().apply {
//                            putLong("analyzeId", analyzeId)
//                        }
//                        val specificFragment = SpecificFragment().apply {
//                            arguments = bundle
//                        }
//
//                        // 상위 Activity의 FragmentManager 사용
//                        val activity = requireActivity()
//                        val containerId = activity.findViewById<View>(android.R.id.content).id
//
//                        Log.d("FragmentNavigation", "트랜잭션 시작: containerId=$containerId")
//
//                        activity.supportFragmentManager.beginTransaction()
//                            .replace(containerId, specificFragment)
//                            .addToBackStack(null)
//                            .commit()
//
//                        Log.d("FragmentNavigation", "트랜잭션 완료")
//                    } else {
//                        Log.e("FragmentNavigation", "analyzeId가 null입니다")
//                    }
//                } else {
//                    Log.e("AllVideoAPI", "응답 실패: ${response.code()} - ${response.message()}")
//                    Toast.makeText(requireContext(), "분석 목록 조회 실패", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                Log.e("AllVideoAPI", "에러 발생: ${e.message}")
//                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }  기존
private fun onAllVideoClicked(gameId: Int) {
    Toast.makeText(requireContext(), "전체영상 클릭됨: $gameId", Toast.LENGTH_SHORT).show()
    val token = TokenManager.getAccessToken() ?: run {
        Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
        return
    }

    lifecycleScope.launch {
        try {
            Log.d("AllVideoAPI", "API 요청 시작 - gameId: $gameId")

            val response = RetrofitClient.specificListApi.getAnalyzeList("Bearer $token", gameId.toLong())

            Log.d("AllVideoAPI", "응답 수신됨 - isSuccessful: ${response.isSuccessful}")

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val body = response.body()
                Log.d("AllVideoAPI", "body: $body")

                val result = body?.result
                Log.d("AllVideoAPI", "result: $result")

                val analyzeList = result?.analyzeList
                Log.d("AllVideoAPI", "analyzeList: $analyzeList")

                val analyzeId = analyzeList?.firstOrNull()?.id
                Log.d("AllVideoAPI", "analyzeId: $analyzeId")

                if (analyzeId != null) {
                    val bundle = Bundle().apply {
                        putLong("analyzeId", analyzeId)
                    }
                    val specificFragment = SpecificFragment().apply {
                        arguments = bundle
                    }

                    val activity = requireActivity()
                    val containerId = activity.findViewById<View>(android.R.id.content).id

                    Log.d("FragmentNavigation", "트랜잭션 시작: containerId=$containerId")

                    activity.supportFragmentManager.beginTransaction()
                        .replace(containerId, specificFragment)
                        .addToBackStack(null)
                        .commit()

                    Log.d("FragmentNavigation", "트랜잭션 완료")
                } else {
                    Log.w("AllVideoAPI", "⚠️ analyzeId가 null입니다. analyzeList가 비어 있거나 데이터 형식 오류 가능성.")
                }
            } else {
                Log.e("AllVideoAPI", "❌ 응답 실패: code=${response.code()} message=${response.message()}")
                Toast.makeText(requireContext(), "분석 목록 조회 실패", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("AllVideoAPI", "예외 발생: ${e.message}", e)
            Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }
}



    // 게임 목록을 가져오는 함수
    private fun fetchGameList(date: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
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
                                val parsedDate = inputFormat.parse(it.playDate)
                                val formattedDate = parsedDate?.let { d -> outputFormat.format(d) } ?: it.playDate
                                AnalyzeItem(formattedDate, it.score.toString(), it.id)
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
