package com.example.cartoonchar.network

import com.example.cartoonchar.network.login.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Header("X-API-KEY") apiKey: String,
        @Field("username") username: String,
        @Field("password") password: String
    ) : LoginResponse

    companion object {

        fun createLoginService(): LoginService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://talentpool.oneindonesia.id/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginService::class.java)
        }
    }
}