package com.example.capston_spotyup.Analyze.Domain

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capston_spotyup.Analyze.DTO.ChartResponse
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentAnalyzeChartBinding

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnalyzeChartFragment : Fragment() {

    private var _binding: FragmentAnalyzeChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AnalyzeViewModel

    private var currentSportsId: Int = 1 // 초기값 (볼링)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeChartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AnalyzeViewModel::class.java)

        val userId = 1234L // 예시 userId

        // 데이터 로드 및 차트 설정
        viewModel.getChartData(userId, currentSportsId)

        // 데이터가 업데이트되면 차트와 RecyclerView를 갱신
        viewModel.chartData.observe(viewLifecycleOwner, { chartData ->
            updateChart(chartData.result.dateScores)
        })

        setupIconSelectors()  // 아이콘 선택 기능 설정
        return binding.root
    }

    // 차트 업데이트
    private fun updateChart(dateScores: List<ChartResponse.getGameDates>) {
        val entries = dateScores.mapIndexed { index, getGameDates ->
            Entry(index.toFloat(), getGameDates.gameScore.toFloat())
        }

        val dataSet = LineDataSet(entries, "월별 평균")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.main)
        dataSet.setCircleColor(Color.BLACK)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(false)

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(false)

        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.axisLeft.axisMinimum = 0f
        binding.lineChart.axisLeft.axisMaximum = 6f

        binding.lineChart.invalidate()  // 차트 업데이트
    }

    // 스포츠 아이콘 클릭 시 데이터 갱신
    private fun setupIconSelectors() {
        val icons = listOf(
            binding.undertab1.getChildAt(0),
            binding.undertab1.getChildAt(1),
            binding.undertab1.getChildAt(2)
        )

        icons.forEachIndexed { index, view ->
            view.setOnClickListener {
                // 모든 아이콘 초기화
                icons.forEach { it.background = null }

                // 선택된 아이콘에 배경 색 지정
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray2))

                // 선택된 스포츠 ID 갱신
                currentSportsId = index + 1
                // 데이터 로드
                val userId = 1234L // 예시 userId
                viewModel.getChartData(userId, currentSportsId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
