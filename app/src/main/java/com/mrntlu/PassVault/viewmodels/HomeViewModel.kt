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

    //TODO: Find a way to insert newly added item to list
    fun addPassword(title: String,username: String, password: String, note: String) {
        viewModelScope.launch {
            homeRepository.addPassword(title, username, password, note).collect {
                if (_passwords.value is Response.Success) {
                    (_passwords.value as Response.Success).data?.plus(it)
                }
            }
        }
    }

    fun getPasswords() {
        viewModelScope.launch {
            homeRepository.getPasswords().collect {
                _passwords.value = it
            }
        }
    }
}