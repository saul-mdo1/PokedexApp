package com.example.pokedexapp.di

import com.example.pokedexapp.presentation.details.DetailsViewModel
import com.example.pokedexapp.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}

val presentationModule = listOf(
    viewModelModule
)