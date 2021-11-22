package com.example.cartoonchar.network.character

import androidx.annotation.Keep
import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.network.model.Info
import com.google.gson.annotations.SerializedName

@Keep
data class CharacterResponse(
    val nextPage: Int? = null,
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val data: List<Character>
)
