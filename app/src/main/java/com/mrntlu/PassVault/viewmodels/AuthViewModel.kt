package com.mrntlu.PassVault.viewmodels

import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.UserRegister
import com.mrntlu.PassVault.services.ParseAuthService
import com.mrntlu.PassVault.utils.Response
import com.parse.ParseUser

class AuthViewModel(
    private val hsViewModel: HomeStateViewModel
): ViewModel(), ParseAuthService {

    init {
        val parseUser = ParseUser.getCurrentUser()

        if (parseUser != null) {
            hsViewModel.state.value = HomeScreenState.LoggedIn
        }
    }

    override fun parseSignout() {
        hsViewModel.uiState.value = Response.Loading

        ParseUser.logOutInBackground { error ->
            if (error == null) {
                hsViewModel.uiState.value = Response.Success(null)
                hsViewModel.state.value = HomeScreenState.Login
            } else {
                hsViewModel.uiState.value = Response.Failure(error)
            }
        }
    }

    override fun parseRegister(userRegister: UserRegister) {
        hsViewModel.uiState.value = Response.Loading

        val user = ParseUser()
        user.apply {
            username = userRegister.username
            setPassword(userRegister.password)
            email = userRegister.email
        }

        user.signUpInBackground { error ->
            if (error == null) {
                hsViewModel.uiState.value = Response.Success(null)
                hsViewModel.state.value = HomeScreenState.Login
            } else {
                hsViewModel.uiState.value = Response.Failure(error)
            }
        }
    }

    override fun parseLogin(username: String, password: String) {
        hsViewModel.uiState.value = Response.Loading

        ParseUser.logInInBackground(
            username, password
        ) { user, error ->
            if (error == null && user != null) {
                hsViewModel.uiState.value = Response.Success(null)
                hsViewModel.state.value = HomeScreenState.LoggedIn
            } else if (error != null) {
                hsViewModel.uiState.value = Response.Failure(error)
            }
        }
    }
}