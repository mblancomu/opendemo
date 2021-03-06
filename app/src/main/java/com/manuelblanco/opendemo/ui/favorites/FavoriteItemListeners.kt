package com.manuelblanco.opendemo.ui.favorites

import com.manuelblanco.core.model.Favorite

interface FavoriteItemListeners {
    fun onFavoriteClickListener(favorite: Favorite)
    fun onFavoriteRemoveListener(favorite: Favorite)
}