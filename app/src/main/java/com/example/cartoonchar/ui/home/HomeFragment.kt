package com.example.cartoonchar.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cartoonchar.Injection
import com.example.cartoonchar.databinding.FragmentHomeBinding
import com.example.cartoonchar.network.model.Character
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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
        homeViewModel =
            ViewModelProvider(requireActivity(), Injection.provideViewModelFactory(owner = this))
                .get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        recyclerView = binding.mainRecyclerView
        recyclerView.addItemDecoration(decorator)

        Log.d("test","test")

        bindScroll(
            pagingData = homeViewModel.pagingDataFlow
        )

        return root
    }

    private fun bindScroll(
        pagingData: Flow<PagingData<Character>>
    ) {
        val characterAdapter = CharacterAdapter()
        recyclerView.adapter = characterAdapter

        val notLoading = characterAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        lifecycleScope.launch {
            pagingData.collectLatest(characterAdapter::submitData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}