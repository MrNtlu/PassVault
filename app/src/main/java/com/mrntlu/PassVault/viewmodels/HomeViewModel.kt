package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.utils.Response
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _passwords = mutableStateOf<Response<List<PasswordItem>>>(Response.Loading)
    val passwords: State<Response<List<PasswordItem>>> = _passwords

    init {
        getPasswords()
    }

    fun getPasswords() {
        viewModelScope.launch {
            homeRepository.getPasswords().collect() {
                _passwords.value = it
            }
        }
    }
}