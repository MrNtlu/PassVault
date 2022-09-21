package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mrntlu.PassVault.utils.Response
import kotlinx.coroutines.flow.MutableStateFlow

class HomeStateViewModel: ViewModel() {
    val state: MutableState<HomeScreenState> = mutableStateOf(HomeScreenState.Login)
    val uiState = MutableStateFlow<Response<Nothing>>(Response.Idle)
}

enum class HomeScreenState {
    LoggedIn,
    Register,
    Login,
}