package com.capstone.healme.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
            btnSignup.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                val passwordConfirmation = edRegisterPasswordConfirmation.text.toString()
                signUpUser(name, email, password, passwordConfirmation)
            }

            btnNavigateLogin.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    ivAnimation to "iv_big",
                    containerDesc to "tv_desc_login",
                    containerField to "field_login"
                )
                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment,
                    null,
                    null,
                    extras
                )
            }
        }
    }

    private fun signUpUser(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {
            if (password == passwordConfirmation) {
                registerViewModel.registerUser(name, email, password)
            } else {
                showToast(resources.getString(R.string.password_not_match))
            }
        } else {
            showToast(resources.getString(R.string.field_not_filled))
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: RegisterViewModel by viewModels {
            factory
        }

        registerViewModel = viewModel

        registerViewModel.registerResponse.observe(viewLifecycleOwner) {
            if (it.error == true) {
                showToast(it.message.toString())
            }
        }
    }

}