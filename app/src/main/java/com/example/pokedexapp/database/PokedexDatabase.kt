package com.example.pokedexapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.database.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun pokedexDao(): PokedexDao
}