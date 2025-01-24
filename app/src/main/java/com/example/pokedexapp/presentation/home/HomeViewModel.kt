package com.example.pokedexapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.repository.PokedexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PokedexRepository) : ViewModel() {
    val loading = MutableLiveData(true)
    val offset = 0

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokedex: LiveData<List<Pokemon>> = _pokemonList

    fun getPokemons() {
        CoroutineScope(Dispatchers.IO).launch {
            val pokemons = repository.get(offset)
            _pokemonList.postValue(pokemons)
            loading.postValue(false)
        }
    }
}