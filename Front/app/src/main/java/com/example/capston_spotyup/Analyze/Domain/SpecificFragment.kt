package com.example.capston_spotyup.Analyze.Domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.databinding.FragmentAnalyzeSpecificBinding
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.Network.RetrofitClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.util.Log



class SpecificFragment : Fragment() {

    private lateinit var binding: FragmentAnalyzeSpecificBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyzeSpecificBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val analyzeId = arguments?.getLong("analyzeId") ?: return
        val token = TokenManager.getAccessToken() ?: return


        fetchSpecificAnalyze(analyzeId, token)
    }

    private fun fetchSpecificAnalyze(analyzeId: Long, token: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.specificApi.getSpecificAnalyze("Bearer $token", analyzeId)

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val result = response.body()?.result
                    binding.textPoseScore.text = result?.poseScore ?: "점수 없음"
                    binding.textRecommendPose.text = result?.recommendPose ?: "추천 자세 없음"

                    result?.videoUrl?.let { url ->
                        binding.videoview.setVideoPath(url)
                        binding.videoview.setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.isLooping = true
                            binding.videoview.start()
                        }
                    }
                } else {
                    Log.e("SpecificAnalyze", "실패: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SpecificAnalyze", "오류 발생: ${e.message}")
            }
        }
    }
}
