package com.capstone.healme.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userRepository: UserRepository): ViewModel() {
    fun setFirstOpen() {
        viewModelScope.launch {
            userRepository.setFirstOpen()
        }
    }
}