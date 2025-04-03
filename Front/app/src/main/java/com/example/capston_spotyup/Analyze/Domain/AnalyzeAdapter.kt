package com.example.capston_spotyup.Analyze.Domain

import DateScore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.coroutines.jvm.internal.CompletedContinuation.context



class AnalyzeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChartBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateScore = dateScores[position]
        holder.bind(dateScore)
    }

    override fun getItemCount(): Int = dateScores.size

    inner class ViewHolder(private val binding: ItemChartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dateScore: DateScore) {
            binding.dateText.text = dateScore.gameDate
            binding.scoreText.text = dateScore.gameScore.toString()
        }
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnalyzeChartFragment()
            1 -> AnalyzeCalendarFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}

