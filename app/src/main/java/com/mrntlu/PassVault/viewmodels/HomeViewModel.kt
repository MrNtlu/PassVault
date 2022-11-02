package com.mrntlu.PassVault.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.toPasswordItem
import com.parse.ParseObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _passwords = mutableStateOf<Response<ArrayList<ParseObject>>>(Response.Loading)
    private var _tempPasswords: ArrayList<ParseObject>? = null
    val passwords: State<Response<List<ParseObject>>> = _passwords

    fun searchPassword(text: String) {
        if (_passwords.value is Response.Success) {
            if (text.isNotEmpty() && text.isNotBlank()) {
                _tempPasswords = (_passwords.value as Response.Success<ArrayList<ParseObject>>).data

                _passwords.value = Response.Success(
                    (_passwords.value as Response.Success<ArrayList<ParseObject>>).data?.filter {
                        it.toPasswordItem().title.startsWith(prefix = text, ignoreCase = true)
                                || it.toPasswordItem().title.endsWith(suffix = text, ignoreCase = true)
                                || it.toPasswordItem().title.contains(text, ignoreCase = true)
                    }?.let {
                        ArrayList(
                            it
                        )
                    }
                )
            } else {
                resetPassword()
            }
        }
    }

    fun resetPassword() {
        if (_passwords.value is Response.Success) {
            _passwords.value = Response.Success(_tempPasswords)
            _tempPasswords = null
        }
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
                                    passwordList.remove(parseObject)

                                    _passwords.value = Response.Success(passwordList)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun editPassword(
        position: Int, title: String, username: String, password: String, note: String?, isEncrypted: Boolean
    ) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data
            val parseObject = passwordList?.get(position)

            parseObject?.let {
                homeRepository.editPassword(it, title, username, password, note, isEncrypted).collect { response ->
                    when(response) {
                        is Response.Loading -> _passwords.value = response
                        is Response.Failure -> _passwords.value = response
                        is Response.Idle -> _passwords.value = response
                        is Response.Success -> {
                            response.data?.let { data ->
                                passwordList[position] = data

                                _passwords.value = Response.Success(passwordList)
                            }
                        }
                    }
                }
            }
        }
    }

    fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data

            homeRepository.addPassword(title, username, password, note, isEncrypted).collect {
                when(it) {
                    is Response.Loading -> _passwords.value = it
                    is Response.Failure -> _passwords.value = it
                    is Response.Idle -> _passwords.value = it
                    is Response.Success -> {
                        it.data?.let { data ->
                            passwordList?.add(data)

                            _passwords.value = Response.Success(passwordList)
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