package com.example.cartoonchar.ui.character

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cartoonchar.databinding.FragmentCharacterBinding
import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.ui.character.recylerview.CharacterAdapter
import com.example.cartoonchar.ui.character.recylerview.CharacterRecyclerViewClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterFragment : Fragment(), CharacterRecyclerViewClickListener {

    private val characterViewModel: CharacterViewModel by viewModels()

    private var _binding: FragmentCharacterBinding? = null

    private val characterAdapter = CharacterAdapter()

    private lateinit var decorator: DividerItemDecoration

    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        recyclerView = binding.mainRecyclerView
        recyclerView.addItemDecoration(decorator)

        Log.d("test", "test")

        binding.bindState(
            uiState = characterViewModel.state,
            pagingData = characterViewModel.pagingDataCharacterFlow,
            uiActions = characterViewModel.accept
        )
        characterAdapter.listener = this

        return root
    }

    override fun onItemClicked(view: View, character: Character?) {
        if (character != null) {
            Toast.makeText(view.context, "Character : ${character.name}", Toast.LENGTH_SHORT).show()
//            characterViewModel.setCharID(character)
            val action =
                CharacterFragmentDirections.actionNavigationHomeToNavigationDetailCharacter2(
                    character.name,
                    character.status,
                    character.location.name,
                    character.image,
                    character.origin.name
                )
            findNavController().navigate(action)
        }
    }

    private fun FragmentCharacterBinding.bindState(
        uiState: StateFlow<CharacterViewModel.UiState>,
        pagingData: Flow<PagingData<Character>>,
        uiActions: (CharacterViewModel.UiAction) -> Unit
    ) {
        recyclerView.adapter = characterAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )

        bindScroll(
            characterAdapter = characterAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentCharacterBinding.updateRepoListFromInput(onQueryChanged: (CharacterViewModel.UiAction.Search) -> Unit) {
        searchRepo.text!!.trim().let {
            if (it.isNotEmpty()) {
                mainRecyclerView.scrollToPosition(0)
                onQueryChanged(CharacterViewModel.UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun FragmentCharacterBinding.bindScroll(
        characterAdapter: CharacterAdapter,
        uiState: StateFlow<CharacterViewModel.UiState>,
        pagingData: Flow<PagingData<Character>>,
        onScrollChanged: (CharacterViewModel.UiAction.Scroll) -> Unit
    ) {

        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(CharacterViewModel.UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val notLoading = characterAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(characterAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) mainRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun FragmentCharacterBinding.bindSearch(
        uiState: StateFlow<CharacterViewModel.UiState>,
        onQueryChanged: (CharacterViewModel.UiAction.Search) -> Unit
    ) {
        searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchRepo::setText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}