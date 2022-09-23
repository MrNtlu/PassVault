package com.mrntlu.PassVault.viewmodels.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirebaseAuthViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
): ViewModel() {

    val signedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val isErrorOccured = mutableStateOf<String?>(null)

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
    }

    fun firebaseSignout() {
        isLoading.value = true

        auth.signOut()
    }

    fun firebaseRegister(email: String, password: String) {
        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.user != null) {
                signedIn.value = true
                isLoading.value = false
            } else {
                handleException(task.exception, "Register failed")
            }
        }.addOnFailureListener {
            handleException(it, "Register failed")
        }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        val exceptionMessage = exception?.localizedMessage ?: ""
        val errorMessage = if (customMessage.isEmpty()) exceptionMessage else "$customMessage: $exceptionMessage"
        isLoading.value = false
        signedIn.value = false
        isErrorOccured.value = errorMessage
    }
}