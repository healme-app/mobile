package com.capstone.healme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.healme.data.UserRepository

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
    fun checkUserState(): LiveData<Boolean> {
        return userRepository.getUserState()
    }
}