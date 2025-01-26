package com.example.pokedexapp.utils

import com.example.pokedexapp.database.entity.PokemonEntity
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.model.Stat
import com.example.pokedexapp.network.response.PokemonResponse
import com.example.pokedexapp.presentation.home.recyclerview.PokemonItemViewModel

object Mapper {
    fun PokemonResponse?.toModel() = Pokemon(
        id = this?.id ?: 0,
        name = this?.name ?: "",
        height = this?.height ?: 0,
        weight = this?.weight ?: 0,
        types = this?.types?.map { typeResponse ->
            typeResponse.type.name
        } ?: emptyList(),
        abilities = this?.abilities?.map { abilityResponse ->
            abilityResponse.ability.name
        } ?: emptyList(),
        stats = this?.stats?.map { statResponse ->
            Stat(statResponse.stat.name, statResponse.base_stat)
        } ?: emptyList(),
        sprite_back_default = this?.sprites?.back_default,
        sprite_front_default = this?.sprites?.front_default,
        front_default = this?.sprites?.other?.home?.front_default,
        moves = this?.moves?.map { moveResponse ->
            moveResponse.move.name
        } ?: emptyList()
    )

    fun PokemonEntity.toModel() = Pokemon(
        id = this.id,
        name = this.name,
        height = this.height,
        weight = this.weight,
        sprite_front_default = this.frontalSprite,
        types = this.types,
        front_default = null,
        sprite_back_default = null,
        abilities = null,
        moves = null,
        stats = null,
        isFavorite = this.isFavorite
    )

    fun PokemonResponse.toEntity() = PokemonEntity(
        id = this.id,
        name = this.name,
        height = this.height,
        weight = this.weight,
        frontalSprite = this.sprites.front_default,
        types = this.types.map {
            it.type.name
        },
    )

    fun Pokemon.toItemViewModel() = PokemonItemViewModel().apply {
        pokemon = this@toItemViewModel
    }
}