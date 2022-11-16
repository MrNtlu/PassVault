package com.mrntlu.PassVault.viewmodels.shared

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(): ViewModel() {

    var selectedImage = mutableStateOf<String?>(null)
        private set

    fun setImage(image: String) {
        selectedImage.value = image
    }

    override fun onCleared() {
        selectedImage.value = null
        super.onCleared()
    }
}