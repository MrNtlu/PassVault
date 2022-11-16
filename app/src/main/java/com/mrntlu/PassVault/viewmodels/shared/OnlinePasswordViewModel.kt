package com.mrntlu.PassVault.viewmodels.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnlinePasswordViewModel @Inject constructor(): ViewModel() {

    var state by mutableStateOf<UIState<PasswordItem>>(UIState.AddItem)
        private set

    fun changeState(newState: UIState<PasswordItem>) {
        state = newState
    }
}