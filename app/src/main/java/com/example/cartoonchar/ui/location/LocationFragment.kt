package com.example.cartoonchar.ui.location

import android.os.Bundle
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
import com.example.cartoonchar.R
import com.example.cartoonchar.databinding.FragmentLocationBinding
import com.example.cartoonchar.network.model.Location
import com.example.cartoonchar.ui.character.CharacterFragmentDirections
import com.example.cartoonchar.ui.location.recyclerview.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.cartoonchar.ui.location.LocationViewModel.UiAction;
import com.example.cartoonchar.ui.location.LocationViewModel.UiState;
import com.example.cartoonchar.ui.location.recyclerview.LocationRecyclerViewClickListener

@AndroidEntryPoint
class LocationFragment : Fragment(), LocationRecyclerViewClickListener {


    private val locationViewModel: LocationViewModel by viewModels()
    private var _binding: FragmentLocationBinding? = null
    private val locationAdapter = LocationAdapter()
    private lateinit var recyclerView: RecyclerView

    private lateinit var decorator: DividerItemDecoration

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        recyclerView = binding.mainRecyclerView
        recyclerView.addItemDecoration(decorator)

        binding.bindState(
            uiState = locationViewModel.state,
            pagingData = locationViewModel.pagingDataLocationFlow,
            uiActions = locationViewModel.accept
        )

        locationAdapter.listener = this
        return root
    }

    override fun onItemClicked(view: View, location: Location?) {
        if (location != null) {
            Toast.makeText(view.context, "Location : ${location.name}", Toast.LENGTH_SHORT).show()
            val action =
                LocationFragmentDirections.actionNavigationLocationToNavigationDetailLocation2(
                    location.name,
                    location.type,
                    location.dimension
                )
            findNavController().navigate(action)
        }
    }

    private fun FragmentLocationBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Location>>,
        uiActions: (UiAction) -> Unit
    ) {
        recyclerView.adapter = locationAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )

        bindScroll(
            locationAdapter = locationAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentLocationBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchRepo.text!!.trim().let {
            if (it.isNotEmpty()) {
                mainRecyclerView.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun FragmentLocationBinding.bindScroll(
        locationAdapter: LocationAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Location>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {

        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val notLoading = locationAdapter.loadStateFlow
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
            pagingData.collectLatest(locationAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) mainRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun FragmentLocationBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
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