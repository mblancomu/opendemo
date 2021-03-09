package com.manuelblanco.core.repository

import androidx.lifecycle.LiveData
import com.manuelblanco.core.local.FavoriteDao
import com.manuelblanco.core.model.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for Favorites calls with suspend methods for coroutines.
 */
class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    val favoriteList = favoriteDao.selectAllFavorites()

    suspend fun addFavorite(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            favoriteDao.insertFavorite(favorite)
        }
    }

    suspend fun removeFavorite(id: Int) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(id)
        }
    }

    suspend fun removeAllFavorites() {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteAllFavorites()
        }
    }

    fun verifyFavorite(id: Int): Boolean {
        return favoriteDao.existFavorite(id) > 0
    }

    fun getFavorites(): LiveData<List<Favorite>> {
        return favoriteDao.selectAllFavorites()
    }
}