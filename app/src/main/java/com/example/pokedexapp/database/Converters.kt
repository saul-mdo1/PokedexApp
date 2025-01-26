package com.example.pokedexapp.database

import androidx.room.TypeConverter
import com.example.pokedexapp.model.Stat

class Converters {
    @TypeConverter
    fun fromTypesList(types: List<String>): String {
        return types.joinToString(",")
    }

    @TypeConverter
    fun toTypesList(typesString: String): List<String> {
        return typesString.split(",")
    }

    @TypeConverter
    fun fromStatList(stats: List<Stat>?): String? {
        return stats?.joinToString(";") { "${it.name}:${it.base_stat}" }
    }

    @TypeConverter
    fun toStatList(statsString: String?): List<Stat>? {
        return statsString?.split(";")?.map {
            val parts = it.split(":")
            Stat(name = parts[0], base_stat = parts[1].toInt())
        }
    }
}