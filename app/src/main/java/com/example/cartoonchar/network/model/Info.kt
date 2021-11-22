package com.example.cartoonchar.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
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
