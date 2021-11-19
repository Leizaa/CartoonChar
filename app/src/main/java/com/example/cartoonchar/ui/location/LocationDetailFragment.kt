package com.example.cartoonchar.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cartoonchar.R
import com.example.cartoonchar.databinding.FragmentDetailLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class LocationDetailFragment : Fragment() {


    private val locationViewModel: LocationViewModel by viewModels()
    private var _binding: FragmentDetailLocationBinding? = null

    val args: LocationDetailFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailLocationBinding.inflate(inflater, container, false)
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
            val type = args.myType
            val dimension = args.myDimension

            tvName.text = name
            tvType.text = type
            tvDimension.text = dimension
            Glide.with(requireContext())
                .load("https://3dwarehouse.sketchup.com/warehouse/v1.0/publiccontent/4326cd28-5b04-485f-82c7-8fae9517648f")
                .placeholder(R.drawable.ic_launcher_foreground)
                .override(400, 200)
                .into(locationImage)
        }
    }
}