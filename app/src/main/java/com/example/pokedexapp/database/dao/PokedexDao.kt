package com.example.pokedexapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedexapp.database.entity.PokemonEntity

@Dao
interface PokedexDao {
    @Query("SELECT * FROM Pokemon LIMIT :limit OFFSET :offset")
    suspend fun getPaginated(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT * FROM Pokemon where id=:id")
    suspend fun getById(id: Int): PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("UPDATE Pokemon SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM Pokemon WHERE id = :id")
    suspend fun isFavoriteById(id: Int): Boolean?
}