package com.example.cartoonchar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.cartoonchar.network.CharacterRepository
import com.example.cartoonchar.network.model.Character
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val repository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<Character>>

    init {
        pagingDataFlow = getCharacterPaging()
    }

    private fun getCharacterPaging(): Flow<PagingData<Character>> =
        repository.getCartoon()
}

