package com.example.cartoonchar.ui.character

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cartoonchar.R
import com.example.cartoonchar.databinding.FragmentDetailCharacterBinding
import com.example.cartoonchar.databinding.FragmentDetailLocationBinding
import com.example.cartoonchar.databinding.FragmentLocationBinding
import com.example.cartoonchar.databinding.FragmentLoginBinding
import com.example.cartoonchar.network.model.Location
import com.example.cartoonchar.ui.location.recyclerview.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.cartoonchar.ui.location.LocationViewModel.UiAction;
import com.example.cartoonchar.ui.location.LocationViewModel.UiState;

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {


    private val characterViewModel: CharacterViewModel by viewModels()
    private var _binding: FragmentDetailCharacterBinding? = null

    val args: CharacterDetailFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val name = args.myName
            val status = args.myStatus
            val location = args.myLocation
            val seen = args.mySeen
            val url = args.myUrl

            tvName.text = name
            tvStatus.text = status
            tvLocation.text = location
            tvSeen.text = seen
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .override(400, 200)
                .into(characterImage)
        }
    }
}