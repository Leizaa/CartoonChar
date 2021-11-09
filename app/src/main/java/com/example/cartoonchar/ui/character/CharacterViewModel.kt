package com.example.cartoonchar.ui.character

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.cartoonchar.network.character.CartoonRepository
import com.example.cartoonchar.network.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CartoonRepository
) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<Character>>

    init {
        pagingDataFlow = getCharacterPaging()
    }

    private fun getCharacterPaging(): Flow<PagingData<Character>> =
        repository.getCartoon()
}

