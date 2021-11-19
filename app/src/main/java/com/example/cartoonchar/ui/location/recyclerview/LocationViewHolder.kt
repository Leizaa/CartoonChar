package com.example.cartoonchar.ui.location.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cartoonchar.R
import com.example.cartoonchar.network.model.Location

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.location_name)
    private val type: TextView = view.findViewById(R.id.location_type)
    private val dimension: TextView = view.findViewById(R.id.location_dimension)
    private val locationImage: ImageView = view.findViewById(R.id.location_image)

    fun bind(location: Location?) {
        if (location == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            type.visibility = View.GONE
            dimension.visibility = View.GONE
        } else {
            showData(location)
        }
    }

    private fun showData(location: Location) {
        name.text = location.name
        type.text = location.type
        dimension.text = location.dimension

        Glide.with(itemView)
            .load("https://3dwarehouse.sketchup.com/warehouse/v1.0/publiccontent/4326cd28-5b04-485f-82c7-8fae9517648f")
            .placeholder(R.drawable.ic_launcher_foreground)
            .override(400, 200)
            .into(locationImage)
    }

    companion object {
        fun create(parent: ViewGroup): LocationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.location_item_view, parent, false)
            return LocationViewHolder(view)
        }
    }
}