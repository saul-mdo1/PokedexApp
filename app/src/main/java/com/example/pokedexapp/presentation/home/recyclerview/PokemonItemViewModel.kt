package com.example.pokedexapp.presentation.home.recyclerview

import com.example.pokedexapp.model.Pokemon

class PokemonItemViewModel {
    var pokemon: Pokemon? = null

    val id: Int
        get() = pokemon?.id ?: 0

    val name: String
        get() = pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "---"

    val sprite: String?
        get() = pokemon?.sprite_front_default

    val isFavorite: Boolean
        get() = pokemon?.isFavorite ?: false
}