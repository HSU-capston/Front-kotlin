package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_spotyup.databinding.ItemAnalayzeRecycleBinding


data class AnalyzeItem(
    val date: String,
    val score: String
)

class AnalyzeSubAdapter : ListAdapter<AnalyzeItem, AnalyzeSubAdapter.AnalyzeViewHolder>(AnalyzeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyzeViewHolder {
        val binding = ItemAnalayzeRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalyzeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalyzeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class AnalyzeViewHolder(private val binding: ItemAnalayzeRecycleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalyzeItem) {
            binding.dateTextView.text = item.date
            binding.scoreTextView.text = item.score
            // 추가적으로 ImageView를 설정하거나 클릭 리스너를 처리할 수 있음
        }
    }

    class AnalyzeItemDiffCallback : DiffUtil.ItemCallback<AnalyzeItem>() {
        override fun areItemsTheSame(oldItem: AnalyzeItem, newItem: AnalyzeItem): Boolean {
            // 항목이 동일한지 비교하는 로직
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: AnalyzeItem, newItem: AnalyzeItem): Boolean {
            // 내용이 동일한지 비교하는 로직
            return oldItem == newItem
        }
    }
}
