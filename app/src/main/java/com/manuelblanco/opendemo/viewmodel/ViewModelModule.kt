package com.manuelblanco.opendemo.viewmodel

import org.koin.dsl.module

/**
 * Module for Koin, in this case for ViewModels.
 */
internal val viewModelModule = module {
    single { CharactersViewModel(get()) }
    single { FavoritesViewModel(get()) }
    single { DetailViewModel(get()) }
}