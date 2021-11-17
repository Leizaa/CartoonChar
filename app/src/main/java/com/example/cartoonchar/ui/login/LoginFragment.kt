package com.example.cartoonchar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cartoonchar.R
import com.example.cartoonchar.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener {
                binding.loginProgressBar.visibility = View.VISIBLE
                viewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString())
            }
        }

        viewModel.snackbar.observe(viewLifecycleOwner){
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.loginStatus.observe(viewLifecycleOwner){
            binding.loginProgressBar.visibility = View.GONE
            if (it) {
                findNavController().navigate(R.id.action_global_navigation_home)
            }
        }
    }
}