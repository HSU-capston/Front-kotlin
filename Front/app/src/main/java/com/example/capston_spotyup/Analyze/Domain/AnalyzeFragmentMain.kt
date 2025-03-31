package com.example.capston_spotyup.Analyze.Domain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.databinding.FragmentAnalyzeMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class AnalyzeFragmentMain : Fragment() {

    private var _binding: FragmentAnalyzeMainBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = listOf("차트", "캘린더")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AnalyzeAdapter(this)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab1, binding.viewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
