package com.example.pokedexapp.repository

import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.utils.Result

interface PokedexRepository {
    suspend fun get(offset: Int): Result<List<Pokemon>>
    suspend fun getById(id: Int): Result<Pokemon>
    suspend fun toggleFavoriteStatus(pokemonId: Int, isFavorite: Boolean)
}