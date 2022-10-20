package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.parseObjectToPasswordItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _passwords = mutableStateOf<Response<List<PasswordItem>>>(Response.Loading)
    val passwords: State<Response<List<PasswordItem>>> = _passwords

    init {
        getPasswords()
    }

    fun addPassword(title: String, username: String, password: String, note: String) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data

            homeRepository.addPassword(title, username, password, note).collect {
                when(it) {
                    is Response.Loading -> _passwords.value = it
                    is Response.Failure -> _passwords.value = it
                    is Response.Idle -> _passwords.value = it
                    is Response.Success -> {
                        it.data?.let { data ->
                            _passwords.value = Response.Success(
                                passwordList?.plus(
                                    parseObjectToPasswordItem(data)
                                )
                            )
                        }
                    }
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