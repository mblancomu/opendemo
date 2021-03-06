package com.manuelblanco.opendemo.ui.list.adapter

import com.manuelblanco.core.model.Character

interface CharacterItemListeners {
    fun onCharacterClickListener(character: Character)
}