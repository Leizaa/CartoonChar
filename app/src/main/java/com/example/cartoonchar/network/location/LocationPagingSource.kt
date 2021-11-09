package com.example.cartoonchar.network.location

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cartoonchar.network.CartoonService
import com.example.cartoonchar.network.model.Location
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class LocationPagingSource(
    private val service: CartoonService
) : PagingSource<Int, Location>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        val position = params.key ?: STARTING_PAGE_INDEX;

        return try {
            val response = service.getLocation(position)
            val locations = response.data

            val nextKey = if (locations.isEmpty()) {
                null
            } else {
                1 + position
            }

            LoadResult.Page(
                data = locations,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            exception.printStackTrace()
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Location>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}