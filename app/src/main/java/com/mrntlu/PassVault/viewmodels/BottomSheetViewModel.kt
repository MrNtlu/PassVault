package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.Red500
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.getItem

class BottomSheetViewModel: ViewModel() {

    var titleState by mutableStateOf("")
    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    var noteState by mutableStateOf("")
    var isEncrypted by  mutableStateOf(true)
    var selectedColor by  mutableStateOf(Red500)

    fun setStateValues(uiState: UIState<PasswordItem>) {
        uiState.getItem().let {
            titleState = it?.title ?: ""
            usernameState = it?.username ?: ""
            passwordState = it?.password ?: ""
            noteState = it?.note ?: ""
            isEncrypted = if (it?.isEncrypted != null) it.isEncrypted!! else uiState is UIState.AddItem
        }
    }

    fun resetValues() {
        titleState = ""
        usernameState = ""
        passwordState = ""
        noteState = ""
        isEncrypted = true
        selectedColor = Red500
    }
}