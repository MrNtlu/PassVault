package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.widgets.SheetState
import com.mrntlu.PassVault.ui.widgets.getItem

class BottomSheetViewModel: ViewModel() {

    var titleState by mutableStateOf("")
    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    var noteState by mutableStateOf("")

    fun setStateValues(sheetState: SheetState<PasswordItem>) {
        sheetState.getItem().let {
            titleState = it?.title ?: ""
            usernameState = it?.username ?: ""
            passwordState = it?.password ?: ""
            noteState = it?.note ?: ""
        }
    }
}