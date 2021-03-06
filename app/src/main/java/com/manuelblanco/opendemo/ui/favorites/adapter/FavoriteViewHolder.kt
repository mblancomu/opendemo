package com.manuelblanco.opendemo.ui.favorites.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.common.ThumbnailSize
import com.manuelblanco.opendemo.databinding.ItemFavoriteBinding
import com.manuelblanco.opendemo.ui.favorites.FavoriteItemListeners

class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemFavoriteBinding.bind(itemView)

    fun bind(favorite: Favorite, listener: FavoriteItemListeners) {
        initListeners(listener, favorite)
        binding.favTitle.text = favorite.name
        binding.favDescription.text = favorite.description
        //Loading the image into the ImageView using Coil
        binding.favImage.apply {
            load(favorite.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.placeholder_marvel)
            }
        }
    }

    /**
     * Listener for items on Favorites list. On Long Click the user can remove a favorite. Its show a popup.
     */
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