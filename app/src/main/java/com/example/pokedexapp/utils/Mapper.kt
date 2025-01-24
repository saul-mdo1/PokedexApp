package com.example.pokedexapp.utils

import com.example.pokedexapp.model.Ability
import com.example.pokedexapp.model.Move
import com.example.pokedexapp.model.Pokemon
import com.example.pokedexapp.model.Stat
import com.example.pokedexapp.model.Type
import com.example.pokedexapp.network.response.PokemonResponse

object Mapper {
    fun PokemonResponse?.toModel() = Pokemon(
        id = this?.id ?: 0,
        name = this?.name ?: "",
        height = this?.height ?: 0,
        weight = this?.weight ?: 0,
        types = this?.types?.map { typeResponse ->
            Type(typeResponse.type.name)
        } ?: emptyList(),
        abilities = this?.abilities?.map { abilityResponse ->
            Ability(abilityResponse.ability.name)
        } ?: emptyList(),
        stats = this?.stats?.map { statResponse ->
            Stat(statResponse.stat.name, statResponse.base_stat)
        } ?: emptyList(),
        sprite_back_default = this?.sprites?.back_default,
        sprite_front_default = this?.sprites?.front_default,
        front_default = this?.sprites?.other?.home?.front_default,
        moves = this?.moves?.map { moveResponse ->
            Move(moveResponse.move.name)
        } ?: emptyList()
    )
}