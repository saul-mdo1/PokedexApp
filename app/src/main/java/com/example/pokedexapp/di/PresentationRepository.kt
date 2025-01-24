package com.example.pokedexapp.di

import com.example.pokedexapp.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

val presentationModule = listOf(
    viewModelModule
)