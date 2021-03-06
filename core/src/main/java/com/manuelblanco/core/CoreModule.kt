package com.manuelblanco.core

import com.manuelblanco.core.local.FavoriteDao
import com.manuelblanco.core.local.localModule
import com.manuelblanco.core.remote.MarvelApi
import com.manuelblanco.core.remote.remoteModule
import com.manuelblanco.core.repository.FavoriteRepository
import com.manuelblanco.core.repository.MarvelRepository
import org.koin.dsl.module

internal val coreModule = module {

    fun provideMarvelRepository(api: MarvelApi): MarvelRepository {
        return MarvelRepository(api)
    }

    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository {
        return FavoriteRepository(dao)
    }

    single { provideMarvelRepository(get()) }
    single { provideFavoriteRepository(get()) }
}

val coreModules = listOf(coreModule, localModule, remoteModule)