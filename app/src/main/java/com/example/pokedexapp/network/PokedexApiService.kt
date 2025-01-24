package com.example.pokedexapp.network

import com.example.pokedexapp.network.response.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexApiService {
    @GET("pokemon")
    suspend fun getAll(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<List<PokemonResponse>>

    @GET("pokemon/{id}")
    suspend fun getById(
        @Path("id") id: Int
    ): Response<PokemonResponse>
}