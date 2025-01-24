package com.example.pokedexapp.repository

import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.network.PokedexApiService
import com.example.pokedexapp.utils.Mapper.toEntity
import com.example.pokedexapp.utils.Mapper.toModel
import com.example.pokedexapp.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.io.IOException

class PokedexRepositoryImpl(
    private val pokedexDao: PokedexDao,
    private val pokedexApi: PokedexApiService
) : PokedexRepository {

    override suspend fun get(offset: Int): Result<List<Pokemon>> = coroutineScope {
        try {
            val listResponse = pokedexApi.getAll(offset = offset, limit = 25)

            if (listResponse.isSuccessful) {
                val basicList = listResponse.body()?.results
                val detailsList = basicList?.map { pokemon ->
                    async {
                        try {
                            pokedexApi.getById(pokemon.url).body()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }?.mapNotNull { it.await() }

                if (!detailsList.isNullOrEmpty()) {
                    pokedexDao.insertAll(detailsList.map { it.toEntity() })
                    Result.Success(detailsList.map { it.toModel() })
                } else {
                    Result.EmptyResponse
                }
            } else {
                Timber.d("PokedexRepositoryImpl_TAG: get: list response error ${listResponse.code()} ")
                Result.Error
            }
        } catch (e: IOException) {
            val offlineData = pokedexDao.getAll()
            if (offlineData.isNotEmpty())
                Result.Success(offlineData.map { it.toModel() })
            else
                Result.NetworkError
        } catch (e: Exception) {
            Timber.d("PokedexRepositoryImpl_TAG: get: list response error: ${e.message} ")
            Result.Error
        }
    }

    override suspend fun getById(id: Int): Pokemon {
        TODO("Not yet implemented")
    }
}