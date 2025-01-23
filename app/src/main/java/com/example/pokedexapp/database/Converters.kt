package com.example.pokedexapp.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromTypesList(types: List<String>): String {
        return types.joinToString(",")
    }

    @TypeConverter
    fun toTypesList(typesString: String): List<String> {
        return typesString.split(",")
    }
}