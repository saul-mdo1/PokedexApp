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
    val emptyList = MutableLiveData<Boolean>()
    private var offset = 0
    var isLoadingMore = MutableLiveData(false)

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokedex: LiveData<List<Pokemon>> = _pokemonList

    fun getPokemons() {
        if (isLoadingMore.value == true) return

        if (offset == 0)
            loading.postValue(true)
        else
            isLoadingMore.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val result = repository.get(offset)) {
                    is Result.Success -> {
                        val pokemons = result.data
                        val currentList = _pokemonList.value?.toMutableList() ?: mutableListOf()
                        currentList.addAll(pokemons)
                        _pokemonList.postValue(currentList)
                        offset += 25
                        emptyList.postValue(false)
                    }
                    is Result.Error -> { errorType.postValue(Result.Error) }
                    is Result.NetworkError -> {
                        if (pokedex.value?.isEmpty() == true)
                            emptyList.postValue(true)
                        errorType.postValue(Result.NetworkError)
                    }
                    is Result.EmptyResponse -> { errorType.postValue(Result.EmptyResponse) }
                }
            } finally {
                loading.postValue(false)
                isLoadingMore.postValue(false)
            }
        }
    }

    fun toggleFavoriteStatus(pokemonId: Int, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.toggleFavoriteStatus(pokemonId, isFavorite)
        }
    }
}