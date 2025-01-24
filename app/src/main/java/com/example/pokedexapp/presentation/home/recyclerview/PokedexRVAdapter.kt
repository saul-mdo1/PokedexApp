package com.example.pokedexapp.presentation.home.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.PokemonItemLayoutBinding
import timber.log.Timber

class PokedexRVAdapter(private val itemClicked: (PokemonItemViewModel) -> Unit) :
    RecyclerView.Adapter<PokedexRVAdapter.ItemViewHolder>() {

    private lateinit var layout: PokemonItemLayoutBinding

    var itemsList: List<PokemonItemViewModel> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ItemViewHolder(val itemBinding: PokemonItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PokemonItemViewModel) {
            itemBinding.root.setOnClickListener {
                itemClicked(item)
            }
            Timber.d("ViewHolder_TAG: bind: ${item.name}")
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