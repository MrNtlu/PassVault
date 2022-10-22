package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.utils.Response
import com.parse.ParseObject
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _passwords = mutableStateOf<Response<List<ParseObject>>>(Response.Loading)
    val passwords: State<Response<List<ParseObject>>> = _passwords

    init {
        getPasswords()
    }

    fun deletePassword(position: Int) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data
            val parseObject = passwordList?.get(position)

            parseObject?.let {
                homeRepository.deletePassword(it).collect { response ->
                    when(response) {
                        is Response.Loading -> _passwords.value = response
                        is Response.Failure -> _passwords.value = response
                        is Response.Idle -> _passwords.value = response
                        is Response.Success -> {
                            response.data?.let { isDeleted ->
                                if (isDeleted) {
                                    _passwords.value = Response.Success(
                                        passwordList.minus(parseObject)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
                                passwordList?.plus(data)
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