package com.example.pokedexapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedexapp.model.Stat

@Entity(tableName = "Pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val frontalSprite: String?,
    @ColumnInfo val height: Int,
    @ColumnInfo val weight: Int,
    @ColumnInfo val types: List<String>,
    @ColumnInfo val abilities: List<String>?,
    @ColumnInfo val stats: List<Stat>?,
    @ColumnInfo val spriteBackDefault: String?,
    @ColumnInfo val spriteFrontDefault: String?,
    @ColumnInfo val frontDefault: String?,
    @ColumnInfo val moves: List<String>?,
    @ColumnInfo val isFavorite: Boolean = false
)
