package com.example.pokedexapp.network

import com.example.pokedexapp.network.response.PokemonListResponse
import com.example.pokedexapp.network.response.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokedexApiService {
    @GET("pokemon")
    suspend fun getAll(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonListResponse>

    @GET
    suspend fun getDetailsByUrl(
        @Url url: String
    ): Response<PokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getDetailsById(
        @Path("id") id: Int
    ): Response<PokemonResponse>
}