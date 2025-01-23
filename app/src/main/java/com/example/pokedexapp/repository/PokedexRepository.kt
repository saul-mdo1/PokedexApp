package com.example.pokedexapp.repository

import com.example.pokedexapp.model.Pokemon

interface PokedexRepository {
    suspend fun getAll(): List<Pokemon>
    suspend fun getById(id: Int): Pokemon
}