package com.mrntlu.PassVault.viewmodels.shared

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainActivitySharedViewModel @Inject constructor(): ViewModel() {

    var shouldShowRemoveAdsDialog = mutableStateOf(false)
        private set

    fun shouldShowDialog(boolean: Boolean) {
        shouldShowRemoveAdsDialog.value = boolean
    }
}