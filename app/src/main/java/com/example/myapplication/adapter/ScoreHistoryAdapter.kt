package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemScoreBinding

class ScoreHistoryAdapter(
    private var scores: List<Pair<String, Int>> = emptyList()
) : RecyclerView.Adapter<ScoreHistoryAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(val binding: ItemScoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val (date, score) = scores[position]
        holder.binding.tvDate.text = date
        holder.binding.tvScore.text = "Score: $score"
    }

    override fun getItemCount(): Int = scores.size

    fun updateScores(newScores: List<Pair<String, Int>>) {
        scores = newScores.sortedByDescending { it.first }
        notifyDataSetChanged()
    }
}
