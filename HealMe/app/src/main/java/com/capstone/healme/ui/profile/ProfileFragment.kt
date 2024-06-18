package com.capstone.healme.ui.profile

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
import com.capstone.healme.databinding.FragmentProfileBinding
import com.capstone.healme.extension.showToast
import com.capstone.healme.helper.modifyDate

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnLogout.setOnClickListener {
                profileViewModel.logoutUser()
            }
            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_ProfileFragment_to_editProfileFragment)
            }
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: ProfileViewModel by viewModels {
            factory
        }

        profileViewModel = viewModel

        profileViewModel.profileResponse.observe(viewLifecycleOwner) { profileResponse ->
            if(profileResponse.error) {
                showToast(profileResponse.message!!, true)
            }
            bindData(profileResponse)
        }
    }

    private fun bindData(profileResponse: ProfileResponse) {
        binding.apply {
            profileResponse.user?.let { profileUser ->
                tvGreetings.text = profileUser.username
                tvUsername.text = profileUser.username
                tvEmail.text = profileUser.email
                tvBirthdate.text = modifyDate(profileUser.dateOfBirth!!)
                tvGender.text = profileUser.gender
                tvWeight.text = profileUser.weight.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getUserProfile()
    }
}