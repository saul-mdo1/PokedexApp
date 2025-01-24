package com.example.pokedexapp.repository

import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.network.PokedexApiService
import com.example.pokedexapp.utils.Mapper.toModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PokedexRepositoryImpl(
    private val pokedexDao: PokedexDao,
    private val pokedexApi: PokedexApiService
) : PokedexRepository {

    override suspend fun get(offset: Int): List<Pokemon> = coroutineScope {
        val listResponse = pokedexApi.getAll(offset = offset, limit = 25)

        val detailsList = listResponse.body()?.results?.map { pokemon ->
            async {
                pokedexApi.getById(pokemon.url).body()
            }
        }?.mapNotNull { it.await() }

        detailsList?.map { it.toModel() } ?: emptyList()
    }

    override suspend fun getById(id: Int): Pokemon {
        TODO("Not yet implemented")
    }
}