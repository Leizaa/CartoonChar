package com.example.cartoonchar.network.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: Data,
    @SerializedName("location")
    val location: Data,
    @SerializedName("image")
    val image: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("episodes")
    val episodes: List<String>
)
