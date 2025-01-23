package com.example.pokedexapp.di

import android.app.Application
import androidx.room.Room
import com.example.pokedexapp.database.PokedexDatabase
import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.repository.PokedexRepository
import com.example.pokedexapp.repository.PokedexRepositoryImpl
import org.koin.dsl.module

fun provideDataBase(application: Application): PokedexDatabase =
    Room.databaseBuilder(
        application,
        PokedexDatabase::class.java,
        "pokedex"
    ).build()

fun providePokedexDao(database: PokedexDatabase): PokedexDao = database.pokedexDao()

val dataBaseModule = module {
    single { provideDataBase(get()) }
    single { providePokedexDao(get()) }
}

val pokedexRepositoryModule = module {
    single<PokedexRepository> { PokedexRepositoryImpl(get()) }
}

val repositoryModule = listOf(
    dataBaseModule,
    pokedexRepositoryModule
)
