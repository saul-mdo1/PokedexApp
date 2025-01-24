package com.example.pokedexapp.repository

import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.network.PokedexApiService
import com.example.pokedexapp.utils.Mapper.toEntity
import com.example.pokedexapp.utils.Mapper.toModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class PokedexRepositoryImpl(
    private val pokedexDao: PokedexDao,
    private val pokedexApi: PokedexApiService
) : PokedexRepository {

    override suspend fun get(offset: Int): List<Pokemon> = coroutineScope {
        try {
            val listResponse = pokedexApi.getAll(offset = offset, limit = 25)

            val detailsList = listResponse.body()?.results?.map { pokemon ->
                async {
                    pokedexApi.getById(pokemon.url).body()
                }
            }?.mapNotNull { it.await() }

            if (!detailsList.isNullOrEmpty()) {
                pokedexDao.insertAll(detailsList.map { it.toEntity() })
            }

            detailsList?.map { it.toModel() } ?: emptyList()
        } catch (e: IOException) {
            val cachedData = pokedexDao.getAll()
            if (cachedData.isNotEmpty())
                cachedData.map { it.toModel() }
            else
                emptyList()
        }
    }

    override suspend fun getById(id: Int): Pokemon {
        TODO("Not yet implemented")
    }
}