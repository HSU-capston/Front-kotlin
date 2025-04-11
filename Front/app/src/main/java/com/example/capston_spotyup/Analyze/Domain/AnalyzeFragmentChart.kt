package com.example.capston_spotyup.Analyze.Domain

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capston_spotyup.Analyze.DTO.Response.ChartResult
import com.example.capston_spotyup.Analyze.DTO.Response.DateScore
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
        viewModel.chartData.observe(viewLifecycleOwner) { chartData ->
            updateChart(chartData.result.dateScores)
            updateStats(chartData.result)  // 📌 추가!
        }

        setupIconSelectors()  // 아이콘 선택 기능 설정
        return binding.root
    }

    // 차트 업데이트
    private fun updateChart(dateScores: List<DateScore>) {
        val entries = dateScores.mapIndexed { index, score ->
            Entry(index.toFloat(), score.gameScore.toFloat())
        }

        val dataSet = LineDataSet(entries, "점수")
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
        binding.lineChart.invalidate()
    }

    private fun updateStats(result: ChartResult) {
        binding.tvGameCount.text = result.gameCount.toString()
        binding.tvAvgScore.text = result.averageScore.toString()
        binding.tvHighScore.text = result.highScore.toString()
        binding.tvLowScore.text = result.lowScore.toString()
    }




    // 스포츠 아이콘 클릭 시 데이터 갱신
    private fun setupIconSelectors() {
        val icons = listOf(
            binding.undertab1.getChildAt(0) as ImageView,
            binding.undertab1.getChildAt(1) as ImageView,
            binding.undertab1.getChildAt(2) as ImageView
        )

        // 선택 아이콘 이미지들
        val selectedIcons = listOf(
            R.drawable.ic_anal_bill,
            R.drawable.ic_anal_golf,
            R.drawable.ic_anal_bowl
        )

        // 기본 아이콘 이미지들
        val defaultIcons = listOf(
            R.drawable.ic_chart_1,
            R.drawable.ic_chart_2,
            R.drawable.ic_chart_3
        )


        icons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                icons.forEachIndexed { i, icon ->
                    icon.setImageResource(defaultIcons[i]) // 기본 아이콘으로 초기화
                }

                imageView.setImageResource(selectedIcons[index]) // 선택된 아이콘만 파란색으로
                currentSportsId = index + 1

                val userId = 1234L // 예시
                viewModel.getChartData(userId, currentSportsId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
