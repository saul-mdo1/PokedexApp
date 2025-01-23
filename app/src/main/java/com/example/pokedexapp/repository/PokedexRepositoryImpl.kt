package com.example.pokedexapp.repository

import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.model.Pokemon

class PokedexRepositoryImpl(private val pokedexDao: PokedexDao) : PokedexRepository {
    override suspend fun getAll(): List<Pokemon> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): Pokemon {
        TODO("Not yet implemented")
    }
}