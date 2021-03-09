package com.manuelblanco.opendemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.core.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model in charge of Favorites from the DB, using ROOM. It is done through a coroutine, where the
 * State is updated and the obtained code is verified. The favoritesData variable is updated
 * with the values, for later use in the fragment that needs it.
 */
class FavoritesViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    lateinit var _favoritesData:LiveData<List<Favorite>>
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    init {
        getFavorites()
    }

    val favoritesData = _favoritesData

    fun removeFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteRepository.removeAllFavorites()
        }
    }

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteRepository.removeFavorite(id)
        }
    }

    fun updateLoadingState(state: LoadingState) {
        _loadingState.value = state
    }

    fun addFavorite(favorite: Favorite) {
        CoroutineScope(Dispatchers.Main).launch {
            if (isFavorite(favorite.id)) {
                favoriteRepository.removeFavorite(favorite.id)
            } else {
                favoriteRepository.addFavorite(favorite)
            }
        }
    }

    fun isFavorite(id: Int): Boolean {
        return favoriteRepository.verifyFavorite(id)
    }

    fun getFavorites() {
        _favoritesData = favoriteRepository.getFavorites()
    }
}