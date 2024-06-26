package com.capstone.healme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.healme.data.UserRepository
import com.capstone.healme.di.Injection
import com.capstone.healme.ui.editprofile.EditProfileViewModel
import com.capstone.healme.ui.healthcare.HealthcareViewModel
import com.capstone.healme.ui.history.HistoryViewModel
import com.capstone.healme.ui.home.HomeViewModel
import com.capstone.healme.ui.login.LoginViewModel
import com.capstone.healme.ui.onboarding.OnboardingViewModel
import com.capstone.healme.ui.profile.ProfileViewModel
import com.capstone.healme.ui.register.RegisterViewModel
import com.capstone.healme.ui.result.ResultViewModel
import com.capstone.healme.ui.scan.ScanViewModel
import com.capstone.healme.ui.updatepassword.UpdatePasswordViewModel

class ViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(HealthcareViewModel::class.java)) {
            return HealthcareViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java)) {
            return UpdatePasswordViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}