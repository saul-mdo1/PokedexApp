package com.example.pokedexapp.network.response

data class PokemonListResponse(
    val results: List<PokemonItemResponse>
)

data class PokemonItemResponse(
    val name: String,
    val url: String
)