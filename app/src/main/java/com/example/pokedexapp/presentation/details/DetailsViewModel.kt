package com.example.pokedexapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.repository.PokedexRepository
import com.example.pokedexapp.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: PokedexRepository) : ViewModel() {
    val loading = MutableLiveData(true)
    val errorType = MutableLiveData<Result<*>?>(null)
    var id: Int = 0

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    val isFavorite = MutableLiveData(false)
    val abilitiesVisibility = MutableLiveData(true)
    val statsVisibility = MutableLiveData(true)
    val movesVisibility = MutableLiveData(true)
    val imageVisibility = MutableLiveData(true)
    fun getDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val result = repository.getById(id)) {
                    is Result.Success -> {
                        val poke = result.data
                        _pokemon.postValue(poke)

                        abilitiesVisibility.postValue(!poke.abilities.isNullOrEmpty())
                        statsVisibility.postValue(!poke.stats.isNullOrEmpty())
                        movesVisibility.postValue(!poke.moves.isNullOrEmpty())
                        imageVisibility.postValue(poke.front_default != null)
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

    fun toggleFavoriteStatus(isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.toggleFavoriteStatus(id, isFavorite)
        }
    }
}