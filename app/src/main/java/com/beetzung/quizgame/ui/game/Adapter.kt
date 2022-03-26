package com.beetzung.quizgame.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beetzung.quizgame.databinding.ItemPlayerBinding

class Adapter(private val data: List<Pair<String, String>>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Pair<String, String>) {
            binding.playerName.text = player.first
            binding.playerPoints.text = player.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size

    }
}