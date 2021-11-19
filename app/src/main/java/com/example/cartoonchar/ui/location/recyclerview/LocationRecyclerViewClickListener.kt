package com.example.cartoonchar.ui.location.recyclerview

import android.view.View
import com.example.cartoonchar.network.model.Location

interface LocationRecyclerViewClickListener {
    fun onItemClicked(view: View, location: Location?)
}