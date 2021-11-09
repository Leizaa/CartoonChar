package com.example.cartoonchar.network.character

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cartoonchar.network.CartoonService
import com.example.cartoonchar.network.model.Character
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CharacterPagingSource(
    private val service: CartoonService
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: STARTING_PAGE_INDEX;

        return try {
            val response = service.getCharacter(position)
            val characters = response.data

            val nextKey = if (characters.isEmpty()) {
                null
            } else {
                1 + position
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            exception.printStackTrace()
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}