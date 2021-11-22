package com.example.cartoonchar.network.location

import androidx.annotation.Keep
import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.network.model.Info
import com.example.cartoonchar.network.model.Location
import com.google.gson.annotations.SerializedName

@Keep
data class LocationResponse(
    val nextPage: Int? = null,
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val data: List<Location>
)
