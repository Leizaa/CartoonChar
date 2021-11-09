package com.example.cartoonchar.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cartoonchar.R
import com.example.cartoonchar.network.model.Character

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.character_name)
    private val status: TextView = view.findViewById(R.id.character_status)
    private val lastSeenLocation: TextView = view.findViewById(R.id.location_text_view)
    private val firstSeenLocation: TextView = view.findViewById(R.id.first_seen_text_view)

    private val characterImage: ImageView = view.findViewById(R.id.character_image)

    fun bind(character: Character?) {
        if (character == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            status.visibility = View.GONE
            lastSeenLocation.visibility = View.GONE
            firstSeenLocation.visibility = View.GONE
        } else {
            showData(character)
        }
    }

    private fun showData(character: Character) {
        name.text = character.name
        val resources = this.itemView.context.resources
        status.text = resources.getString(R.string.status_format, character.status, character.species)
        lastSeenLocation.text = character.location.name
        firstSeenLocation.text = character.origin.name

        Glide.with(itemView)
            .load(character.image)
            .into(characterImage)
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item_view, parent, false)
            return CharacterViewHolder(view)
        }
    }
}