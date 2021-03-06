package com.manuelblanco.opendemo.ui.favorites.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.databinding.ItemFavoriteBinding
import com.manuelblanco.opendemo.ui.favorites.FavoriteItemListeners

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemFavoriteBinding.bind(itemView)

    fun bind(favorite: Favorite, listener: FavoriteItemListeners) {
        initListeners(listener, favorite)
        binding.favTitle.text = favorite.name
        binding.favDescription.text = favorite.description
        binding.favImage.load(favorite.thumbnail){
            placeholder(R.drawable.placeholder_marvel)
        }
    }

    private fun initListeners(listener: FavoriteItemListeners, favorite: Favorite) {
        binding.root.setOnClickListener {
            listener.onFavoriteClickListener(favorite)
        }

        binding.root.setOnLongClickListener {
            listener.onFavoriteRemoveListener(favorite)
            true
        }
    }

}