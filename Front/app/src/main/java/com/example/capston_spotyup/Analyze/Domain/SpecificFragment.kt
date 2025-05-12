package com.example.capston_spotyup.Analyze.Domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.capston_spotyup.databinding.FragmentAnalyzeSpecificBinding
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.Analyze.DTO.Response.AnalyzeListItem
import kotlinx.coroutines.launch
import android.util.Log
import com.example.capston_spotyup.Analyze.DTO.Response.AnalyzeDetailResult
import com.example.capston_spotyup.R   // ✅ 반드시 우리 프로젝트 R을 import!

class SpecificFragment : Fragment() {

    private lateinit var binding: FragmentAnalyzeSpecificBinding
    private var analyzeList: List<AnalyzeListItem> = emptyList()
    private var selectedAnalyzeId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAnalyzeSpecificBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyzeList = arguments?.getParcelableArrayList("analyzeList") ?: emptyList()
        selectedAnalyzeId = arguments?.getLong("initialAnalyzeId") ?: return
        val token = TokenManager.getAccessToken() ?: return

        setupSpinner()
        fetchSpecificAnalyze(selectedAnalyzeId, token)
    }

    private fun setupSpinner() {
        val analyzeIdStrings = analyzeList.map { "분석 ID: ${it.id}" }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, analyzeIdStrings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.listSpinner.adapter = adapter

        // Spinner 현재 선택을 초기 설정
        binding.listSpinner.setSelection(analyzeList.indexOfFirst { it.id == selectedAnalyzeId })

        //  setOnItemSelectedListener 는 반드시 이렇게
        binding.listSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selected = analyzeList[position]
                selectedAnalyzeId = selected.id
                val token = TokenManager.getAccessToken() ?: return
                fetchSpecificAnalyze(selectedAnalyzeId, token)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 것도 선택하지 않았을 때는 무시
            }
        }
    }

    private fun setSeekbar(
        seekstatus: Int?,
        movestatus: Int?,
        wristStatus: Int?,
        ankleStatus: Int?,
    ) {
        val seekpoint: Int? = seekstatus
        val movepoint: Int? = movestatus
        val wristpoint: Int? = wristStatus
        val anklepoint: Int? = ankleStatus

        if (seekpoint != null) {
            binding.feedbackBar.isEnabled =false
            binding.feedbackBar.progress = seekpoint
        }
        if (movepoint != null) {
            binding.feedbackBar2.isEnabled =false
            binding.feedbackBar2.progress = movepoint
        }
        if (wristpoint != null) {
            binding.feedbackBar3.isEnabled =false
            binding.feedbackBar3.progress = wristpoint
        }
        if (anklepoint != null) {
            binding.feedbackBar4.isEnabled =false
            binding.feedbackBar4.progress = anklepoint
        }
    }

    private fun fetchSpecificAnalyze(analyzeId: Long, token: String) {
        lifecycleScope.launch {
            try {
                val response =
                    RetrofitClient.specificApi.getSpecificAnalyze("Bearer $token", analyzeId)

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val result = response.body()?.result
                    binding.textPoseScore.text = result?.poseScore ?: "점수 없음"
                    binding.textRecommendPose.text = result?.recommendPose ?: "추천 자세 없음"
                    binding.textGoodPoint.text = result?.goodPoint ?: "좋지않음"
                    binding.textBadPoint.text = result?.badPoint ?: "좋지않음"

                    binding.score.text = result?.score.toString()
                    binding.shoulder.text = result?.shoulderAngleDiff.toString()
                    binding.movement.text = result?.movementDistance.toString()
                    binding.wrist.text = result?.wristMovementTotal.toString()
                    binding.ankle.text = result?.ankleSwitchCount.toString()
                    setSeekbar(
                        result?.shoulderAngleDiff,
                        result?.movementDistance,
                        result?.wristMovementTotal,
                        result?.ankleSwitchCount
                    )




                    result?.videoUrl?.let { url ->
                        binding.videoview.setVideoPath(url)
                        binding.videoview.setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.isLooping = true
                            binding.videoview.start()
                        }
                    }
                } else {
                    Log.e("SpecificAnalyze", "API 실패: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SpecificAnalyze", "오류 발생: ${e.message}")
            }
        }
    }
}
