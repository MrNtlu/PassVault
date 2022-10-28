package com.mrntlu.PassVault.viewmodels.offline

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.repositories.OfflineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineViewModel @Inject constructor(
    private val repository: OfflineRepository
): ViewModel() {

    private var _passwords = mutableStateOf<RealmResults<OfflinePassword>?>(null)
    private var _tempPasswords: RealmResults<OfflinePassword>? = null
    val password: State<RealmResults<OfflinePassword>?> = _passwords

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startMigration()
        }
    }

    fun searchPassword(text: String) {
        _passwords.value?.let {
            if (text.isNotEmpty() && text.isNotBlank()) {
                _tempPasswords = it

                _passwords.value = repository.searchPassword(text)
            } else {
                resetPassword()
            }
        }
    }

    fun resetPassword() {
        _passwords.value?.let {
            _passwords.value = _tempPasswords
            _tempPasswords = null
        }
    }

    fun getOfflinePasswords() {
        _passwords.value = repository.getAllPasswords()
    }

    fun addPassword(idMail: String, password: String, description: String) {
        viewModelScope.launch {
            repository.addPassword(idMail, description, password)
            getOfflinePasswords()
        }
    }

    fun editPassword(position: Int, idMail: String, password: String, description: String) {
        _passwords.value?.let {
            viewModelScope.launch {
                repository.editPassword(it[position], idMail, description, password)
                getOfflinePasswords()
            }
        }
    }

    fun deletePassword(position: Int) {
        _passwords.value?.let {
            viewModelScope.launch {
                repository.deletePassword(it[position])
                getOfflinePasswords()
            }
        }
    }

    fun deleteAllPassword() {
        viewModelScope.launch {
            repository.deleteAllPasswords()
            getOfflinePasswords()
        }
    }

    override fun onCleared() {
        repository.onCleared()
        super.onCleared()
    }
}