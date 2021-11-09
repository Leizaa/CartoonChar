package com.example.cartoonchar.ui.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.cartoonchar.network.model.Character

class CharacterAdapter : PagingDataAdapter<Character, CharacterViewHolder>(CHARACTER_COMPARATOR) {

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterItem = getItem(position)
        if (characterItem != null) {
            holder.bind(characterItem)
        }
    }
}