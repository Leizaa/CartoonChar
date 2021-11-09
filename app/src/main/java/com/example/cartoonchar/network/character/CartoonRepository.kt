package com.example.cartoonchar.network.character

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cartoonchar.network.CartoonService
import com.example.cartoonchar.network.location.LocationPagingSource
import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.network.model.Location
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartoonRepository @Inject constructor(private val service: CartoonService) {
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

    fun getCartoonLocation(): Flow<PagingData<Location>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                LocationPagingSource(service)
            }
        ).flow
    }
}