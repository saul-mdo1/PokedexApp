package com.example.pokedexapp.presentation.home.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.PokemonItemLayoutBinding

class PokedexRVAdapter(
    private val itemClicked: (PokemonItemViewModel) -> Unit,
    private val imageClicked: (PokemonItemViewModel, imageView: View) -> Unit,
    private val favoriteClicked: (PokemonItemViewModel, Boolean) -> Unit
) :
    RecyclerView.Adapter<PokedexRVAdapter.ItemViewHolder>() {

    private lateinit var layout: PokemonItemLayoutBinding
    private val itemsList: MutableList<PokemonItemViewModel> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newItems: List<PokemonItemViewModel>) {
        val previousSize = itemsList.size
        itemsList.addAll(newItems)
        notifyItemRangeInserted(previousSize, newItems.size)
    }

    inner class ItemViewHolder(val itemBinding: PokemonItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PokemonItemViewModel) {
            itemBinding.root.setOnClickListener {
                itemClicked(item)
            }

            itemBinding.ivSprite.setOnClickListener {
                imageClicked(item, it)
            }

            itemBinding.icon.setOnCheckedChangeListener { _, isChecked ->
                favoriteClicked(item, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        layout = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pokemon_item_layout,
            parent,
            false
        )
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val pokemon = itemsList[position]
        holder.itemBinding.viewModel = pokemon
        holder.bind(pokemon)
        holder.itemBinding.executePendingBindings()
    }
}