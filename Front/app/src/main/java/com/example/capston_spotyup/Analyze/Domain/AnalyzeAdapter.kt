package com.example.capston_spotyup.Analyze.Domain

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AnalyzeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnalyzeChartFragment()
            1 -> AnalyzeCalendarFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}
