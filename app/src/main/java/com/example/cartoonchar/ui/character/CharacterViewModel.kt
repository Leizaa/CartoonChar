package com.example.cartoonchar.ui.character

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cartoonchar.network.character.CartoonRepository
import com.example.cartoonchar.network.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CartoonRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var characterID = MutableLiveData<Character?>()

    fun setCharID(charID: Character?) {
        characterID.value = charID
    }

    fun getCharID(): MutableLiveData<Character?> {
        return characterID
    }

    val state: StateFlow<CharacterViewModel.UiState>

    val pagingDataCharacterFlow: Flow<PagingData<Character>>

    val accept: (CharacterViewModel.UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<CharacterViewModel.UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<CharacterViewModel.UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(CharacterViewModel.UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<CharacterViewModel.UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(CharacterViewModel.UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataCharacterFlow = searches
            .flatMapLatest { getCharacterPaging(queryString = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            CharacterViewModel.UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = CharacterViewModel.UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    sealed class UiAction {
        data class Search(val query: String) : UiAction()
        data class Scroll(val currentQuery: String) : UiAction()
    }

    data class UiState(
        val query: String = DEFAULT_QUERY,
        val lastQueryScrolled: String = DEFAULT_QUERY,
        val hasNotScrolledForCurrentSearch: Boolean = false
    )

    private fun getCharacterPaging(queryString: String): Flow<PagingData<Character>> =
        repository.getCartoon(queryString)
}

private const val DEFAULT_QUERY = "Rick"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"

