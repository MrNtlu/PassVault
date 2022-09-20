package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeScreenViewModel: ViewModel() {

    val state: MutableState<HomeScreenState> = mutableStateOf(HomeScreenState.Register)
}

enum class HomeScreenState {
    LoggedIn,
    Register,
    Login,
}