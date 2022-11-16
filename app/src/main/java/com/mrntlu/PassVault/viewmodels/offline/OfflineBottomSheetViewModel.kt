package com.mrntlu.PassVault.viewmodels.offline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.getItem

class OfflineBottomSheetViewModel: ViewModel() {

    var idMailState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    var descriptionState by mutableStateOf("")

    fun setStateValues(uiState: UIState<OfflinePassword>) {
        uiState.getItem().let {
            idMailState = it?.idMail ?: ""
            passwordState = it?.password ?: ""
            descriptionState = it?.description ?: ""
        }
    }

    fun resetValues() {
        idMailState = ""
        passwordState = ""
        descriptionState = ""
    }
}