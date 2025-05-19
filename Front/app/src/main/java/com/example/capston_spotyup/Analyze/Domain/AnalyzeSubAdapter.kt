package com.example.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_spotyup.databinding.ItemAnalayzeRecycleBinding


data class AnalyzeItem(
    val date: String,
    val score: String,
    val gameId: Int
)


class AnalyzeSubAdapter(
    private val onAllVideoClick: (Int) -> Unit,
    private val lookHighlight: (Int) -> Unit
) : ListAdapter<AnalyzeItem, AnalyzeSubAdapter.AnalyzeViewHolder>(AnalyzeItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyzeViewHolder {
        val binding = ItemAnalayzeRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalyzeViewHolder(binding, onAllVideoClick, lookHighlight)
    }

    override fun onBindViewHolder(holder: AnalyzeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AnalyzeViewHolder(
        private val binding: ItemAnalayzeRecycleBinding,
        private val onAllVideoClick: (Int) -> Unit,
        private val lookHighlight: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalyzeItem) {
            binding.dateTextView.text = item.date
            binding.scoreTextView.text = "${item.score}점"


            binding.allVideoButton.setOnClickListener {
                onAllVideoClick(item.gameId)
            }
            binding.highlight.setOnClickListener{
                lookHighlight(item.gameId)
            }
        }
    }

    class AnalyzeItemDiffCallback : DiffUtil.ItemCallback<AnalyzeItem>() {
        override fun areItemsTheSame(oldItem: AnalyzeItem, newItem: AnalyzeItem): Boolean {
            return oldItem.gameId == newItem.gameId
        }

        override fun areContentsTheSame(oldItem: AnalyzeItem, newItem: AnalyzeItem): Boolean {
            return oldItem == newItem
        }
    }
}

