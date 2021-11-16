package com.example.cartoonchar.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cartoonchar.R
import com.example.cartoonchar.SingletonTimer
import com.example.cartoonchar.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

import com.google.android.material.bottomnavigation.BottomNavigationView




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
                viewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString())
            }
        }

        viewModel.snackbar.observe(viewLifecycleOwner){
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.loginStatus.observe(viewLifecycleOwner){
            if (it) {
                SingletonTimer.init()

            }
        }
    }
}