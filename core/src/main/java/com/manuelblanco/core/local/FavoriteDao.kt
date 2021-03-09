package com.manuelblanco.core.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manuelblanco.core.model.Favorite

/**
 * Favorite Dao for manage the DB Room.
 */
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite_table")
    fun deleteAllFavorites()

    @Query("DELETE FROM favorite_table WHERE id = :id")
    fun deleteFavorite(id: Int)

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun selectFavorite(id: Int): LiveData<Favorite>

    @Query("SELECT * FROM favorite_table ORDER BY name ASC")
    fun selectAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT COUNT(*) from favorite_table WHERE id = :id")
    fun existFavorite(id: Int): Int
}