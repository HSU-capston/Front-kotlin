package com.example.capston_spotyup.Analyze.Domain

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentAnalyzeChartBinding

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnalyzeChartFragment : Fragment() {

    private var _binding: FragmentAnalyzeChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupChart()
        setupIconSelectors()
    }


    private fun setupChart() {
        val entries = listOf(
            Entry(1f, 2f), Entry(2f, 3f), Entry(3f, 1f),
            Entry(4f, 4f), Entry(5f, 3f), Entry(6f, 5f)
        )

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

        binding.lineChart.invalidate()
    }

    private fun setupIconSelectors() {
        val icons = listOf(
            binding.undertab1.getChildAt(0),
            binding.undertab1.getChildAt(1),
            binding.undertab1.getChildAt(2)
        )

        icons.forEachIndexed { index, view ->
            view.setOnClickListener {
                icons.forEachIndexed { i, v ->
                    v.background = null
                }
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray2))
                // 선택한 차트에 맞는 데이터 갱신 로직 필요시 여기에 구현
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
