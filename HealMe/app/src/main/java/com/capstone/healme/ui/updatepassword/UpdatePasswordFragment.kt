package com.capstone.healme.ui.updatepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.databinding.FragmentUpdatePasswordBinding
import com.capstone.healme.extension.setLoading
import com.capstone.healme.extension.showToast
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class UpdatePasswordFragment : Fragment() {
    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var updatePasswordViewModel: UpdatePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpdatePasswordViewModel by viewModels {
            factory
        }

        updatePasswordViewModel = viewModel

        updatePasswordViewModel.updateResponse.observe(viewLifecycleOwner) { updateResponse ->
            if (!updateResponse.error) {
                showToast(getString(R.string.update_password_success), false)
                findNavController().popBackStack()
            } else {
                showToast(getString(R.string.update_password_failed, updateResponse.message), false)
            }
        }

        updatePasswordViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(binding.progressBar, isLoading)
        }
    }

    private fun setupAction() {
        binding.btnSaveChanges.setOnClickListener {
            binding.apply {
                if (!newPasswordEditTextLayout.isErrorEnabled && !oldPasswordEditTextLayout.isErrorEnabled) {
                    val oldPassword = edOldPassword.text.toString()
                    val newPassword = edNewPassword.text.toString()
                    updatePassword(oldPassword, newPassword)
                } else {
                    showToast(resources.getString(R.string.fix_field), false)
                }
            }
        }
    }

    private fun updatePassword(oldPassword: String, newPassword: String) {
        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
            val oldPasswordBody = oldPassword.toRequestBody("text/plain".toMediaType())
            val newPasswordBody = newPassword.toRequestBody("text/plain".toMediaType())
            updatePasswordViewModel.updatePassword(oldPasswordBody, newPasswordBody)
        } else {
            showToast(resources.getString(R.string.field_not_filled), true)
        }
    }
}