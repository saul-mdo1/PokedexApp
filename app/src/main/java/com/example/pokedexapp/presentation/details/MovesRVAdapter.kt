package com.example.pokedexapp.presentation.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R

class MovesRVAdapter(private val moves: List<String>) : RecyclerView.Adapter<MovesRVAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moveNameTextView: TextView = itemView.findViewById(R.id.tvMoveName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.move_item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val move = moves[position]
        holder.moveNameTextView.text = move
    }

    override fun getItemCount(): Int = moves.size
}