package com.mrntlu.PassVault.viewmodels.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.UserRegister
import com.mrntlu.PassVault.services.ParseAuthService
import com.parse.ParseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParseAuthViewModel @Inject constructor(): ViewModel(), ParseAuthService {

    val isSignedIn = mutableStateOf(false)
    val isRegistered = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val isErrorOccured = mutableStateOf<String?>(null)

    init {
        val parseUser = ParseUser.getCurrentUser()
        isSignedIn.value = parseUser != null
    }

    override fun parseSignout() {
        isLoading.value = true

        ParseUser.logOutInBackground { error ->
            if (error == null) {
                isLoading.value = false
                isSignedIn.value = false
            } else {
                handleException(error)
            }
        }
    }

    override fun parseForgotPassword(email: String, onSuccess: () -> Unit) {
        isLoading.value = true

        ParseUser.requestPasswordResetInBackground(email) { error ->
            if (error == null) {
                isLoading.value = false
                onSuccess()
            } else {
                handleException(error)
            }
        }
    }

    override fun parseRegister(userRegister: UserRegister) {
        userRegister.apply {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                handleException(customMessage = "Please don't leave anything empty.")
                return
            }
        }

        isLoading.value = true

        val user = ParseUser()
        user.apply {
            username = userRegister.username
            setPassword(userRegister.password)
            email = userRegister.email
        }

        user.signUpInBackground { error ->
            if (error == null) {
                isLoading.value = false
                isRegistered.value = true
            } else {
                handleException(error)
            }
        }
    }

    override fun parseLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please don't leave anything empty.")
            return
        }

        isLoading.value = true

        ParseUser.logInInBackground(
            username, password
        ) { user, error ->
            if (error == null && user != null) {
                isLoading.value = false
                isSignedIn.value = true
            } else if (error != null) {
                handleException(error)
            }
        }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        val errorMessage = if (customMessage.isEmpty())
            exception?.localizedMessage ?: ""
        else
            "$customMessage ${if (exception?.localizedMessage != null) ": ${exception.localizedMessage}" else ""}"
        isLoading.value = false
        isSignedIn.value = false
        isErrorOccured.value = errorMessage
    }
}