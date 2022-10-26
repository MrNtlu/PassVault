package com.mrntlu.PassVault.viewmodels.offline

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.repositories.OfflineRepository
import com.mrntlu.PassVault.utils.printLog
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
    val password: State<RealmResults<OfflinePassword>?> = _passwords

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startMigration()
        }
    }

    fun getOfflinePasswords() {
        _passwords.value = repository.getAllPasswords()
        printLog(message = "Get ${_passwords.value}")
    }

    fun addPassword(idMail: String, description: String, password: String) {
        viewModelScope.launch {
            repository.addPassword(idMail, description, password)
            getOfflinePasswords()
        }
    }

    override fun onCleared() {
        repository.onCleared()
        super.onCleared()
    }
}