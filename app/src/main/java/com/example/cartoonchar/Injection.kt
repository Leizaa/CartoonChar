package com.example.cartoonchar

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.cartoonchar.network.CartoonService
import com.example.cartoonchar.network.CharacterRepository
import com.example.cartoonchar.ui.ViewModelFactory

object Injection {
    private fun provideCharacterRepository(): CharacterRepository {
        return CharacterRepository(CartoonService.create())
    }

    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory{
        return ViewModelFactory(owner, provideCharacterRepository())
    }
}