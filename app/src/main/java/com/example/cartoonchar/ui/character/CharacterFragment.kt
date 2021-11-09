package com.example.cartoonchar.ui.character

import android.os.Bundle
import android.util.Log
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
import com.example.cartoonchar.databinding.FragmentCharacterBinding
import com.example.cartoonchar.network.model.Character
import com.example.cartoonchar.ui.character.recylerview.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by viewModels()

    private var _binding: FragmentCharacterBinding? = null

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

        Log.d("test","test")

        bindScroll(
            pagingData = characterViewModel.pagingDataFlow
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