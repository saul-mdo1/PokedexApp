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
    val loading = MutableLiveData(true)
    val errorType = MutableLiveData<Result<*>?>(null)
    val offset = 0

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokedex: LiveData<List<Pokemon>> = _pokemonList

    fun getPokemons() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                when (val result = repository.get(offset)) {
                    is Result.Success -> {
                        val pokemons = result.data
                        _pokemonList.postValue(pokemons)
                    }
                    is Result.Error -> { errorType.postValue(Result.Error) }
                    is Result.NetworkError -> { errorType.postValue(Result.NetworkError) }
                    is Result.EmptyResponse -> { errorType.postValue(Result.EmptyResponse) }
                }
            } finally {
                loading.postValue(false)
            }
        }
    }
}