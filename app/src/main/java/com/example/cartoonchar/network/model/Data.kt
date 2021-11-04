package com.example.cartoonchar.network.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
