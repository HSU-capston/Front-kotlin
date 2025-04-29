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
import com.example.capston_spotyup.Analyze.DTO.Request.ChartRequest
import com.example.capston_spotyup.Analyze.DTO.Response.ChartResult
import com.example.capston_spotyup.Analyze.DTO.Response.DateScore
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentAnalyzeChartBinding

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

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
        val request = ChartRequest(
            userId = userId,
            sportsId = currentSportsId,
            date = "2025-04-30",  // 원하는 날짜를 입력
            token = "your-auth-token"  // 실제 인증 토큰을 넣어야 합니다.
        )

// ChartRequest 객체를 ViewModel에 전달
        viewModel.getChartData(request)

        // 데이터가 업데이트되면 차트와 통계 정보를 갱신
        viewModel.chartData.observe(viewLifecycleOwner) { chartData ->
            chartData?.result?.dateScores?.let {
                updateChart(it)
                updateStats(chartData.result)
            }
        }

        setupIconSelectors()  // 아이콘 선택 기능 설정
        return binding.root
    }

    // 차트 업데이트
    private fun updateChart(dateScores: List<DateScore>) {
        val entries = dateScores.mapIndexed { index, score ->
            Entry(index.toFloat(), score.gameScore.toFloat())
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M/d", Locale.getDefault())

        val labels = dateScores.map {
            val dateOnly = it.gameDate.substring(0, 10)
            try {
                val parsed = inputFormat.parse(dateOnly)
                outputFormat.format(parsed!!)
            } catch (e: Exception) {
                dateOnly
            }
        }

        val dataSet = LineDataSet(entries, "점수").apply {
            color = ContextCompat.getColor(requireContext(), R.color.main)
            setCircleColor(Color.BLACK)
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false) // true로 바꾸면 그래프 위에 점수 생김
            valueTextSize = 10f
            valueTextColor = Color.BLACK
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return "\n${entry?.y?.toInt()}"  // 그래프 위에 뜨는 점수 줄바꿈 트릭
                }
            }
        }
        binding.lineChart.setExtraOffsets(0f, 30f, 0f, 10f) // 그래프 위로 공간 확보


        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in labels.indices) labels[index] else ""
            }
        }

        binding.lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = formatter
            granularity = 1f
            setDrawGridLines(false)
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.axisLeft.axisMinimum = 0f
        binding.lineChart.description.isEnabled = false
        binding.lineChart.invalidate()

        val markerView = CustomMarkerView(requireContext(), R.layout.marker_chart)
        markerView.chartView = binding.lineChart  // 필수!
        binding.lineChart.marker = markerView
    }


    private fun updateStats(result: ChartResult) {
        binding.tvGameCount.text = result.gameCount.toString()
        binding.tvAvgScore.text = result.averageScore.toInt().toString()
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
                val request = ChartRequest(
                    userId = userId,
                    sportsId = currentSportsId,
                    date = "2025-04-30",  // 원하는 날짜를 입력
                    token = "your-auth-token"  // 실제 인증 토큰을 넣어야 합니다.
                )

// ChartRequest 객체를 ViewModel에 전달
                viewModel.getChartData(request)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
