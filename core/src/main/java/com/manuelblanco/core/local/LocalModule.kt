package com.manuelblanco.core.local

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Module for Koin with the local DB Room.
 */
internal val localModule = module {

    fun provideDatabase(application: Application): LocalDB {
        return Room.databaseBuilder(application, LocalDB::class.java, "localDb")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideFavoriteDao(database: LocalDB): FavoriteDao {
        return database.favoriteDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideFavoriteDao(get()) }
}