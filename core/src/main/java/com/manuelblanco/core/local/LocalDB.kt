package com.manuelblanco.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manuelblanco.core.model.Favorite

@Database(
    entities = arrayOf(
        Favorite::class
    ),
    version = 1
)
abstract class LocalDB : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
