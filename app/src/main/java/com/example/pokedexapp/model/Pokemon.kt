package com.example.pokedexapp.model

data class Pokemon(
    val id: Int,
    val name: String,
    val frontalSprite: String,
    val height: Int,
    val weight: Int,
    val types: List<String>
)
