package com.capstone.healme.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentLoginBinding
import com.capstone.healme.extension.setLoading
import com.capstone.healme.extension.showToast
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )

        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation


        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                if (!emailEditTextLayout.isErrorEnabled && !passwordEditTextLayout.isErrorEnabled) {
                    loginUser(email, password)
                } else {
                    showToast(resources.getString(R.string.fix_field), true)
                }
            }

            btnNavigateSignup.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    ivAnimation to "iv_small",
                    containerDesc to "tv_desc_register",
                    containerField to "field_register"
                )
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment,
                    null,
                    null,
                    extras
                )
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val emailBody = email.toRequestBody("text/plain".toMediaType())
            val passwordBody = password.toRequestBody("text/plain".toMediaType())
            loginViewModel.login(emailBody, passwordBody)
        } else {
            showToast(resources.getString(R.string.field_not_filled), false)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: LoginViewModel by viewModels {
            factory
        }

        loginViewModel = viewModel

        loginViewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it.error) {
                showToast(getString(R.string.login_error, it.message), false)
            } else {
                val token = it.loginResult?.token!!
                val id = it.loginResult.userId!!
                loginViewModel.setUserLogin(token, id)
                showToast(getString(R.string.login_success), false)
                findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
            }
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(binding.progressBar, isLoading)
        }
    }
}