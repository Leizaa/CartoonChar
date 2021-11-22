package com.example.cartoonchar.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
