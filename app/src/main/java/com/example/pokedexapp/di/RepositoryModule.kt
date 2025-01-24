package com.example.pokedexapp.di

import android.app.Application
import androidx.room.Room
import com.example.pokedexapp.database.PokedexDatabase
import com.example.pokedexapp.database.dao.PokedexDao
import com.example.pokedexapp.network.PokedexApiService
import com.example.pokedexapp.repository.PokedexRepository
import com.example.pokedexapp.repository.PokedexRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://pokeapi.co/api/v2/"

//region DATABASE
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
//endregion

//region RETROFIT
inline fun <reified T> createRetrofitWebService(url: String): T =
    Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(T::class.java)

val networkModule = module {
    single<PokedexApiService> { createRetrofitWebService(BASE_URL) }
}
//endregion

val repositoriesModule = module {
    single<PokedexRepository> { PokedexRepositoryImpl(get(), get()) }
}

val repositoryModule = listOf(
    dataBaseModule,
    networkModule,
    repositoriesModule
)
