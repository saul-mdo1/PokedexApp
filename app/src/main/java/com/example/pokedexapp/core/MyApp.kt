package com.example.pokedexapp.core

import android.app.Application
import com.example.pokedexapp.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Timber.d("MyApp_TAG: onCreate: ")

        startKoin {
            androidContext(this@MyApp)
            modules(repositoryModule)
        }
    }
}