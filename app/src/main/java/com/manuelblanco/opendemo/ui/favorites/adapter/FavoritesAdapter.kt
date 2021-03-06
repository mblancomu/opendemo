package com.manuelblanco.opendemo.ui.favorites.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.ui.favorites.FavoriteItemListeners

class FavoritesAdapter() :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    var favorites: MutableList<Favorite> = mutableListOf()
    var listener: FavoriteItemListeners? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        listener?.let { listener -> holder.bind(favorites[position], listener) }
    }

    fun removeItem(favorite: Favorite) {
        favorites.remove(favorite)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun addFavorites(list: MutableList<Favorite>) {
        favorites = list
        notifyDataSetChanged()
    }
}
