package com.capstone.healme.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.entity.ScanEntity

class HistoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllHistories(): LiveData<List<ScanEntity>> {
        return userRepository.getAllHistory()
    }
}