package com.example.cartoonchar.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cartoonchar.databinding.FragmentLocationBinding
import com.example.cartoonchar.network.model.Location
import com.example.cartoonchar.ui.location.recyclerview.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private val locationViewModel: LocationViewModel by viewModels()
    private var _binding: FragmentLocationBinding? = null
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

        bindScroll(
            pagingData = locationViewModel.pagingDataLocationFlow
        )
        return root
    }

    private fun bindScroll(
        pagingData: Flow<PagingData<Location>>
    ) {
        val locationAdapter = LocationAdapter()
        recyclerView.adapter = locationAdapter

        val notLoading = locationAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        lifecycleScope.launch {
            pagingData.collectLatest(locationAdapter::submitData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}