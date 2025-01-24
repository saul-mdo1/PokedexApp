package com.example.pokedexapp.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val abilities: List<Ability>?,
    val stats: List<Stat>?,
    val sprite_back_default: String?,
    val sprite_front_default: String?,
    val front_default: String?,
    val moves: List<Move>?
)

data class Type(
    val name: String
)

data class Ability(
    val name: String
)

data class Stat(
    val name: String,
    val base_stat: Int
)

data class Move(
    val name: String
)
