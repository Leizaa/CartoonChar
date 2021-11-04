package com.example.cartoonchar.network.model

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("count")
    val count : Int,
    @SerializedName("pages")
    val pages : String,
    @SerializedName("next")
    val next : String? = "",
    @SerializedName("prev")
    val prev : String? = ""
)
