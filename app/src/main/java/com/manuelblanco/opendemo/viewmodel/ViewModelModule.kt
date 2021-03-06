package com.manuelblanco.opendemo.viewmodel

import org.koin.dsl.module

internal val viewModelModule = module {
    single { CharactersViewModel(get()) }
    single { FavoritesViewModel(get()) }
    single { DetailViewModel(get()) }
}