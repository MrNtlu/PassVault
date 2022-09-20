package com.mrntlu.PassVault.viewmodels

import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.UserRegister
import com.parse.ParseUser

class AuthViewModel(
    homeScreenViewModel: HomeScreenViewModel
): ViewModel() {

    init {
        val parseUser = ParseUser.getCurrentUser()

        if (parseUser != null) {
            //homeScreenViewModel.state.value = HomeScreenState.LoggedIn
        }
    }

    fun onSignup(user: UserRegister) {

    }
}