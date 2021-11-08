package com.example.cartoonchar.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.cartoonchar.network.CharacterRepository
import com.example.cartoonchar.network.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<Character>>

    init {
        pagingDataFlow = getCharacterPaging()
    }

    private fun getCharacterPaging(): Flow<PagingData<Character>> =
        repository.getCartoon()
}

