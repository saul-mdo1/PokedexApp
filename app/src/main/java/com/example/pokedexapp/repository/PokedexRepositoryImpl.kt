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
            val apiResponse = pokedexApi.getAll(offset = offset, limit = 25)

            if (apiResponse.isSuccessful) {
                val basicList = apiResponse.body()?.results
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
                    // Save api response to database
                    val entities = detailsList.map {
                        it.toEntity().copy(isFavorite = pokedexDao.isFavoriteById(it.id) == true)
                    }
                    pokedexDao.insertAll(entities)
                } else {
                    // Api returns nothing
                    Result.EmptyResponse
                }

                // Return the list from the database, not from the api
                val pokemons = pokedexDao.getPaginated(limit = 25, offset = offset)
                Result.Success(pokemons.map { it.toModel() })
            } else {
                // API Call error
                Timber.d("PokedexRepositoryImpl_TAG: get: list response error ${apiResponse.code()} ")
                Result.Error
            }
        } catch (e: IOException) {
            // No internet connection, return from database
            val offlineData = pokedexDao.getPaginated(limit = 25, offset = offset)
            if (offlineData.isNotEmpty())
                Result.Success(offlineData.map { it.toModel() })
            else
                Result.NetworkError
        } catch (e: Exception) {
            // Unexpected error
            Timber.d("PokedexRepositoryImpl_TAG: get: list response error: ${e.message} ")
            Result.Error
        }
    }

    override suspend fun getById(id: Int): Result<Pokemon> = coroutineScope {
        try {
            val offlineData = pokedexDao.getById(id)

            // If the pokemon exists in the database, pass it directly
            if (offlineData != null)
                return@coroutineScope Result.Success(offlineData.toModel())

            // If doesn't exist, call the api
            val response = pokedexApi.getDetailsById(id)
            if (response.isSuccessful) {
                Result.Success(response.body().toModel())
            } else {
                Timber.d("PokedexRepositoryImpl_TAG: getById: response error ${response.code()} ")
                Result.Error
            }
        } catch (e: IOException) {
            val offlineData = pokedexDao.getById(id)
            return@coroutineScope if (offlineData != null) {
                Result.Success(offlineData.toModel())
            } else {
                Timber.d("PokedexRepositoryImpl_TAG: getById: IOException, no data offline available.")
                Result.Error
            }
        } catch (e: Exception) {
            Timber.d("PokedexRepositoryImpl_TAG: getById: list response error: ${e.message} ")
            Result.Error
        }
    }

    override suspend fun toggleFavoriteStatus(pokemonId: Int, isFavorite: Boolean) {
        pokedexDao.updateFavoriteStatus(pokemonId, isFavorite)
    }

}