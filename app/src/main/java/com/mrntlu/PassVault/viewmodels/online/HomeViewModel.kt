package com.mrntlu.PassVault.viewmodels.online

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.toPasswordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _passwords = mutableStateOf<Response<MutableList<PasswordItem>>>(Response.Loading)
    private var _tempPasswords: MutableList<PasswordItem>? = null
    val passwords: State<Response<List<PasswordItem>>> = _passwords

    private val _uiResponse = mutableStateOf<Response<Nothing>>(Response.Idle)
    val uiResponse: State<Response<Nothing>> = _uiResponse

    fun searchPassword(text: String) {
        if (_passwords.value is Response.Success) {
            if (text.isNotEmpty() && text.isNotBlank()) {
                _tempPasswords = (_passwords.value as Response.Success<MutableList<PasswordItem>>).data

                _passwords.value = Response.Success(
                    (_passwords.value as Response.Success<MutableList<PasswordItem>>).data?.filter {
                        it.title.startsWith(prefix = text, ignoreCase = true)
                                || it.title.endsWith(suffix = text, ignoreCase = true)
                                || it.title.contains(text, ignoreCase = true)
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
            val passwordItem = passwordList?.get(position)

            passwordItem?.let {
                homeRepository.deletePassword(it).collect { response ->
                    when(response) {
                        is Response.Loading -> _passwords.value = response
                        is Response.Failure -> _passwords.value = response
                        is Response.Idle -> _passwords.value = response
                        is Response.Success -> {
                            response.data?.let { isDeleted ->
                                if (isDeleted) {
                                    passwordList.remove(passwordItem)

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
        position: Int, title: String, username: String, password: String,
        note: String?, isEncrypted: Boolean, imageUri: String?, imageColor: String,
    ) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data
            val parseObject = passwordList?.get(position)

            parseObject?.let {
                homeRepository.editPassword(
                    it, title, username, password,
                    note, isEncrypted, imageUri, imageColor,
                ).collect { response ->
                    when(response) {
                        is Response.Loading -> _uiResponse.value = response
                        is Response.Failure -> _uiResponse.value = response
                        is Response.Idle -> _uiResponse.value = response
                        is Response.Success -> {
                            response.data?.let { data ->
                                passwordList[position] = data.toPasswordItem()

                                _passwords.value = Response.Success(passwordList)
                            }
                            _uiResponse.value = Response.Success(null)
                        }
                    }
                }
            }
        }
    }

    fun addPassword(
        title: String, username: String, password: String,
        note: String?, isEncrypted: Boolean, imageUri: String?,
        imageColor: String,
    ) {
        viewModelScope.launch {
            val passwordList = (_passwords.value as Response.Success).data

            homeRepository.addPassword(
                title, username, password, note,
                isEncrypted, imageUri, imageColor,
            ).collect {
                when(it) {
                    is Response.Loading -> _uiResponse.value = it
                    is Response.Failure -> _uiResponse.value = it
                    is Response.Idle -> _uiResponse.value = it
                    is Response.Success -> {
                        it.data?.let { data ->
                            passwordList?.add(data.toPasswordItem())

                            _passwords.value = Response.Success(passwordList)
                        }
                        _uiResponse.value = Response.Success(null)
                    }
                }
            }
        }
    }

    fun getPasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getPasswordsOrCache().collect {
                _passwords.value = it
            }
        }
    }

    fun resetUIResponse() {
        _uiResponse.value = Response.Idle
    }
}