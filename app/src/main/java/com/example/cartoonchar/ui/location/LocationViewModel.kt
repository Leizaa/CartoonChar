package com.example.cartoonchar.ui.location

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.cartoonchar.network.character.CartoonRepository
import com.example.cartoonchar.network.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: CartoonRepository
) : ViewModel() {

    val pagingDataLocationFlow: Flow<PagingData<Location>>

    init {
        pagingDataLocationFlow = getLocationPaging()
    }

    private fun getLocationPaging(): Flow<PagingData<Location>> =
        repository.getCartoonLocation()
}

