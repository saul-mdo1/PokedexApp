package com.example.pokedexapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.repository.PokedexRepository
import com.example.pokedexapp.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PokedexRepository) : ViewModel() {
    val loading = MutableLiveData(false)
    val errorType = MutableLiveData<Result<*>?>(null)
    private var offset = 0
    private var isLoadingMore = false

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokedex: LiveData<List<Pokemon>> = _pokemonList

    fun getPokemons() {
        if (isLoadingMore) return

        isLoadingMore = true
        loading.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {
            try{
                when (val result = repository.get(offset)) {
                    is Result.Success -> {
                        val pokemons = result.data
                        val currentList = _pokemonList.value?.toMutableList() ?: mutableListOf()
                        currentList.addAll(pokemons)
                        _pokemonList.postValue(currentList)
                        offset += 25
                    }
                    is Result.Error -> { errorType.postValue(Result.Error) }
                    is Result.NetworkError -> { errorType.postValue(Result.NetworkError) }
                    is Result.EmptyResponse -> { errorType.postValue(Result.EmptyResponse) }
                }
            } finally {
                loading.postValue(false)
                isLoadingMore = false
            }
        }
    }
}