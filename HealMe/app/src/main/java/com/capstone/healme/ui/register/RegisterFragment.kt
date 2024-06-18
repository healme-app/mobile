package com.capstone.healme.ui.register

import android.app.AlertDialog
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
import com.capstone.healme.databinding.FragmentRegisterBinding
import com.capstone.healme.extension.setLoading
import com.capstone.healme.extension.showToast
import com.capstone.healme.helper.DatePickerFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class RegisterFragment : Fragment(), DatePickerFragment.DialogDateListener {

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
                val name = edRegisterName.text.toString().trim()
                val birthDate = edRegisterBirthdate.text.toString().trim()
                val gender = edRegisterGender.text.toString().trim()
                val weight = edRegisterWeight.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()
                val passwordConfirmation = edRegisterPasswordConfirmation.text.toString().trim()

                val inputFields =
                    listOf(name, birthDate, gender, weight, email, password, passwordConfirmation)

                if (validateFields(inputFields)) {
                    signUpUser(name, birthDate, gender, weight, email, password, passwordConfirmation)
                }
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

            edRegisterBirthdate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(childFragmentManager, "DatePicker")
            }

            edRegisterGender.setOnClickListener {
                setupGenderSelector()
            }
        }
    }

    private fun validateFields(inputFields: List<String>): Boolean {
        for (field in inputFields) {
            if (field.isEmpty()) {
                showToast(resources.getString(R.string.field_not_filled), false)
                return false
            }
        }
        return true
    }

    private fun signUpUser(
        name: String,
        birthDate: String,
        gender: String,
        weight: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        if (password == passwordConfirmation) {
            val nameBody = name.toRequestBody("text/plain".toMediaType())
            val birthDateBody = birthDate.toRequestBody("text/plain".toMediaType())
            val genderBody = gender.toRequestBody("text/plain".toMediaType())
            val weightBody = weight.toRequestBody("text/plain".toMediaType())
            val emailBody = email.toRequestBody("text/plain".toMediaType())
            val passwordBody = password.toRequestBody("text/plain".toMediaType())
            registerViewModel.registerUser(nameBody, birthDateBody, genderBody, weightBody, emailBody, passwordBody)
        } else {
            showToast(resources.getString(R.string.password_not_match), false)
        }

    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: RegisterViewModel by viewModels {
            factory
        }

        registerViewModel = viewModel

        registerViewModel.registerResponse.observe(viewLifecycleOwner) {
            if (it.userId?.isNotEmpty() == true) {
                showToast(getString(R.string.successfully_create_account), true)
                binding.btnNavigateLogin.performClick()
            } else {
                showToast(getString(R.string.failed_to_create_account), true)
            }
        }

        registerViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            setLoading(binding.progressBar, isLoading)
        }
     }

    private fun setupGenderSelector() {
        val genderOptions = arrayOf(resources.getString(R.string.male), resources.getString(R.string.female))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.select_gender))

        builder.setItems(genderOptions) { _, which ->
            binding.edRegisterGender.setText(genderOptions[which])
        }

        builder.show()
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val birthDate = getString(R.string.birth_date, year, month, dayOfMonth)
        binding.edRegisterBirthdate.setText(birthDate)
    }

}