package com.example.pokedexapp.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val abilities: List<String>?,
    val stats: List<Stat>?,
    val sprite_back_default: String?,
    val sprite_front_default: String?,
    val front_default: String?,
    val moves: List<String>?,
    val isFavorite: Boolean = false
)

data class Stat(
    val name: String,
    val base_stat: Int
)
