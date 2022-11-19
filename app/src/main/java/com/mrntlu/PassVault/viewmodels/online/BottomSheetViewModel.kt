package com.mrntlu.PassVault.viewmodels.online

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.getItem

class BottomSheetViewModel: ViewModel() {

    var titleState by mutableStateOf("")
    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    var noteState by mutableStateOf("")
    var isEncrypted by mutableStateOf(true)
    var selectedColor by mutableStateOf(Color.Black)

    var titleError by mutableStateOf(false)
    var titleErrorMessage by mutableStateOf("")

    var usernameError by mutableStateOf(false)
    var usernameErrorMessage by mutableStateOf("")

    var passwordError by mutableStateOf(false)
    var passwordErrorMessage by mutableStateOf("")

    fun setStateValues(uiState: UIState<PasswordItem>) {
        uiState.getItem().let {
            titleState = it?.title ?: ""
            usernameState = it?.username ?: ""
            passwordState = it?.getDecryptedPassword() ?: ""
            noteState = it?.note ?: ""
            isEncrypted = if (it?.isEncrypted != null) it.isEncrypted!! else uiState is UIState.AddItem
            if (it != null)
                selectedColor = Color(it.imageColor.toULong())
        }
    }

    fun resetValues() {
        titleState = ""
        usernameState = ""
        passwordState = ""
        noteState = ""
        isEncrypted = true
        selectedColor = Color.Black

        titleError = false
        titleErrorMessage = ""

        usernameError = false
        usernameErrorMessage = ""

        passwordError = false
        passwordErrorMessage = ""
    }
}