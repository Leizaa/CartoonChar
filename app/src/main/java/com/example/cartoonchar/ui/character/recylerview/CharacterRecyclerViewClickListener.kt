package com.example.cartoonchar.ui.character.recylerview

import android.view.View
import com.example.cartoonchar.network.model.Character

interface CharacterRecyclerViewClickListener {
    fun onItemClicked(view: View, character: Character?)
}