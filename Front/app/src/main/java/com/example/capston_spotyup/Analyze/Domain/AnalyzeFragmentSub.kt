package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager


import com.example.capston_spotyup.databinding.FragmentAnalyzeSubBinding

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

        // 더미 데이터 설정
        val items = listOf(
            AnalyzeItem("25.03.10", "180점"),
            AnalyzeItem("25.03.11", "200점"),
            AnalyzeItem("25.03.12", "220점")
        )

        analyzeAdapter.submitList(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
