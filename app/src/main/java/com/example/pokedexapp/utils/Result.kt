package com.example.pokedexapp.utils
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
    object NetworkError : Result<Nothing>()
    object EmptyResponse : Result<Nothing>()
}