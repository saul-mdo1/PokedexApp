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
                            pokedexApi.getDetailsByUrl(pokemon.url).body()
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
            val offlineData = pokedexDao.getPaginated(limit = 25, offset = offset)
            if (offlineData.isNotEmpty())
                Result.Success(offlineData.map { it.toModel() })
            else
                Result.NetworkError
        } catch (e: Exception) {
            Timber.d("PokedexRepositoryImpl_TAG: get: list response error: ${e.message} ")
            Result.Error
        }
    }

    override suspend fun getById(id: Int): Result<Pokemon> = coroutineScope {
        try {
            val response = pokedexApi.getDetailsById(id)
            if (response.isSuccessful) {
                Result.Success(response.body().toModel())
            } else {
                Timber.d("PokedexRepositoryImpl_TAG: getById: response error ${response.code()} ")
                Result.Error
            }
        } catch (e: IOException) {
            val offlineData = pokedexDao.getById(id)
            Result.Success(offlineData.toModel())
        } catch (e: Exception) {
            Timber.d("PokedexRepositoryImpl_TAG: getById: list response error: ${e.message} ")
            Result.Error
        }
    }

}