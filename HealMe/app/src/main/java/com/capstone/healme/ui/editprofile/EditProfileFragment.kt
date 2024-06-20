package com.capstone.healme.ui.editprofile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.healme.R
import com.capstone.healme.ViewModelFactory
import com.capstone.healme.data.remote.response.ProfileResponse
import com.capstone.healme.databinding.FragmentEditProfileBinding
import com.capstone.healme.extension.showToast
import com.capstone.healme.helper.DatePickerFragment
import com.capstone.healme.helper.modifyDate
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class EditProfileFragment : Fragment(), DatePickerFragment.DialogDateListener {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var editProfileViewModel: EditProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            edEditBirthdate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(childFragmentManager, "DatePicker")
            }

            edEditGender.setOnClickListener {
                setupGenderSelector()
            }

            btnSaveChanges.setOnClickListener {
                val name = edEditName.text.toString().toRequestBody("text/plain".toMediaType())
                val birthdate =
                    edEditBirthdate.text.toString().toRequestBody("text/plain".toMediaType())
                val gender = edEditGender.text.toString().toRequestBody("text/plain".toMediaType())
                val weight = edEditWeight.text.toString().toRequestBody("text/plain".toMediaType())

                editProfileViewModel.updateProfile(name, birthdate, gender, weight)
            }

        }
    }

    private fun setupGenderSelector() {
        val genderOptions = arrayOf("laki-laki", "perempuan")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Gender")

        builder.setItems(genderOptions) { _, which ->
            binding.edEditGender.setText(genderOptions[which])
        }

        builder.show()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EditProfileViewModel by viewModels {
            factory
        }

        editProfileViewModel = viewModel

        editProfileViewModel.profileResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
                bindCurrentProfile(it)
            }
        }

        editProfileViewModel.editProfileResponse.observe(viewLifecycleOwner) {
            if (!it.error) {
                showToast(getString(R.string.update_profile_success), false)
                findNavController().popBackStack()
            } else {
                showToast(getString(R.string.update_profile_error, it.message), false)
            }
        }
    }

    private fun bindCurrentProfile(profileResponse: ProfileResponse) {
        binding.apply {
            profileResponse.user?.let {
                edEditName.setText(it.username)
                edEditBirthdate.setText(modifyDate(it.dateOfBirth!!))
                edEditGender.setText(it.gender)
                edEditWeight.setText(it.weight.toString())
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val birthDate = getString(R.string.birth_date, year, month, dayOfMonth)
        binding.edEditBirthdate.setText(birthDate)
    }
}