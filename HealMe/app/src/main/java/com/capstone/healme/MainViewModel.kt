package com.capstone.healme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun checkUserState(): LiveData<Boolean> {
        return userRepository.getUserState()
    }

    fun checkFirstOpen(): LiveData<Boolean> {
        return userRepository.getFirstOpen()
    }
}