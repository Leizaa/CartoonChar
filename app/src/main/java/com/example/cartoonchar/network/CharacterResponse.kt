package com.example.cartoonchar.network

import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.network.model.Info
import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    val nextPage: Int? = null,
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val data: List<Character>
)
