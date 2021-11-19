package com.example.cartoonchar.network

import com.example.cartoonchar.network.character.CharacterResponse
import com.example.cartoonchar.network.location.LocationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CartoonService {

    @GET("character")
    suspend fun getCharacter(
        @Query("name") query: String,
        @Query("page") page: Int
    ): CharacterResponse

    @GET("location")
    suspend fun getLocation(
        @Query("name") query: String,
        @Query("page") page: Int
    ): LocationResponse

    companion object {

        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create(): CartoonService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CartoonService::class.java)
        }
    }
}