package com.manuelblanco.opendemo.ui.list.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manuelblanco.core.model.Character
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.databinding.ItemCharacterBinding

class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemCharacterBinding.bind(itemView)

    fun bind(character: Character, listener: CharacterItemListeners) {
        initListeners(listener, character)
        val imageUrl = character.thumbnail.path.plus("/portrait_xlarge").plus(".")
            .plus(character.thumbnail.extension)
        binding.ivThumbnail.apply {
            load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_marvel)
            }
        }
    }

    private fun initListeners(listener: CharacterItemListeners, character: Character) {
        binding.root.setOnClickListener {
            listener.onCharacterClickListener(character)
        }
    }
}