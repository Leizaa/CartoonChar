package com.example.cartoonchar

import com.example.cartoonchar.network.CartoonService
import com.example.cartoonchar.network.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Provides
    fun provideCartoonService(): CartoonService {
        return CartoonService.create()
    }

    @Provides
    fun provideLoginService(): LoginService {
        return LoginService.createLoginService()
    }
}