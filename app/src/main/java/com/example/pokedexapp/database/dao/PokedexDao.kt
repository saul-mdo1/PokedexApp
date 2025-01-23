package com.example.pokedexapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedexapp.database.entity.PokemonEntity

@Dao
interface PokedexDao {
    @Query("SELECT * FROM Pokemon")
    suspend fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM Pokemon where id=:id")
    suspend fun getById(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)
}