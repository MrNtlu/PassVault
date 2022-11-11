package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.getItem

class BottomSheetViewModel: ViewModel() {

    var titleState by mutableStateOf("")
    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    var noteState by mutableStateOf("")
    var isEncrypted by  mutableStateOf(true)

    fun setStateValues(sheetState: SheetState<PasswordItem>) {
        sheetState.getItem().let {
            titleState = it?.title ?: ""
            usernameState = it?.username ?: ""
            passwordState = it?.password ?: ""
            noteState = it?.note ?: ""
            isEncrypted = if (it?.isEncrypted != null) it.isEncrypted!! else false
        }
    }

    fun resetValues() {
        titleState = ""
        usernameState = ""
        passwordState = ""
        noteState = ""
        isEncrypted = true
    }
}