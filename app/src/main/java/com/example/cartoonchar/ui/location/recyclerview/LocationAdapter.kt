package com.example.cartoonchar.ui.location.recyclerview

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.cartoonchar.network.model.Location

class LocationAdapter : PagingDataAdapter<Location, LocationViewHolder>(LOCATION_COMPARATOR) {

    var listener : LocationRecyclerViewClickListener? = null

    companion object {
        private val LOCATION_COMPARATOR = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationItem = getItem(position)
        if (locationItem != null) {
            holder.bind(locationItem)
        }
        holder.itemView.setOnClickListener {
            listener?.onItemClicked(it,locationItem)
        }
    }
}