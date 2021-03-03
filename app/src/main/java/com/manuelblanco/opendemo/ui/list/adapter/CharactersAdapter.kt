package com.manuelblanco.opendemo.ui.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manuelblanco.core.model.Character

class CharactersAdapter: ListAdapter<Character, RecyclerView.ViewHolder>(AsyncDifferConfig.Builder(ListItemCallback()).build()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class ListItemCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return false
    }
}