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
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var layout: PokemonItemLayoutBinding
    private val itemsList: MutableList<PokemonItemViewModel> = mutableListOf()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var isLoading = false

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

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            layout = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.pokemon_item_layout,
                parent,
                false
            )
            ItemViewHolder(layout)
        } else {
            val progressBarLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item_layout, parent, false)
            LoadingViewHolder(progressBarLayout)
        }
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val pokemon = itemsList[position]
            holder.itemBinding.viewModel = pokemon
            pokemon.let { holder.bind(it) }
            holder.itemBinding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsList[position].pokemon == null)
            VIEW_TYPE_LOADING
        else
            VIEW_TYPE_ITEM
    }

    fun showLoading(isLoading: Boolean) {
        if (this.isLoading == isLoading) return

        this.isLoading = isLoading
        if (isLoading) {
            if (isLastItemValid()) {
                itemsList.add(PokemonItemViewModel())
                notifyItemInserted(itemsList.size - 1)
            }
        } else {
            val loadingItem = itemsList.firstOrNull { it.pokemon == null }
            if (loadingItem != null) {
                val index = itemsList.indexOf(loadingItem)
                itemsList.remove(loadingItem)
                notifyItemRemoved(index)
            }
        }
    }

    private fun isLastItemValid() = itemsList.isEmpty() || itemsList.last().pokemon != null

    fun updateItemFavoriteStatus(item: PokemonItemViewModel) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            itemsList[index] = item
            notifyItemChanged(index)
        }
    }
}