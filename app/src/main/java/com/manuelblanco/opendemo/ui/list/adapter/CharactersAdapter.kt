package com.manuelblanco.opendemo.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manuelblanco.core.model.Character
import com.manuelblanco.opendemo.R

class CharactersAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    var characters: MutableList<Character> = mutableListOf()
    var listener: CharacterItemListeners? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        listener?.let { listener -> holder.bind(characters[position], listener) }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun addCharacters(list: MutableList<Character>) {
        characters = list
        notifyDataSetChanged()
    }
}