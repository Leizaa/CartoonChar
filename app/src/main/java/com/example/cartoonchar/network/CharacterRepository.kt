package com.example.cartoonchar.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cartoonchar.network.model.Character
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val service: CartoonService) {
    fun getCartoon(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharacterPagingSource(service)
            }
        ).flow
    }
}