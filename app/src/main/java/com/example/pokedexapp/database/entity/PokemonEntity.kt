package com.example.pokedexapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val frontalSprite: String,
    @ColumnInfo val height: Int,
    @ColumnInfo val weight: Int,
    @ColumnInfo val types: List<String>
)
