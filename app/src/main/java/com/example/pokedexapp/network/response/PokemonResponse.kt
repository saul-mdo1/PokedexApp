package com.example.pokedexapp.network.response

data class PokemonResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeItemResponse>,
    val abilities: List<AbilityItemResponse>,
    val stats: List<StatItemResponse>,
    val sprites: SpritesResponse,
    val moves: List<MoveItemResponse>
)
data class TypeItemResponse(
    val type: TypeResponse
)
data class TypeResponse(
    val name: String
)
data class AbilityItemResponse(
    val ability: AbilityResponse
)
data class AbilityResponse(
    val name: String
)
data class StatItemResponse(
    val base_stat: Int,
    val stat: StatResponse
)
data class StatResponse(
    val name: String
)
data class SpritesResponse(
    val back_default: String?,
    val front_default: String?,
    val other: OtherResponse?,
)
data class OtherResponse(
    val home: HomeResponse
)
data class HomeResponse(
    val front_default: String
)
data class MoveItemResponse(
    val move: MoveResponse
)
data class MoveResponse(
    val name: String
)

