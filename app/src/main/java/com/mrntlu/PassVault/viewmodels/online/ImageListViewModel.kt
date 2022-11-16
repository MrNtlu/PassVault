package com.mrntlu.PassVault.viewmodels.online

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.PassVault.models.ImageListItem
import com.mrntlu.PassVault.repositories.ImageListRepository
import com.mrntlu.PassVault.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: ImageListRepository,
): ViewModel() {

    private val _imageList = mutableStateOf<Response<List<ImageListItem>>>(Response.Idle)
    val imageList: State<Response<List<ImageListItem>>> = _imageList

    fun getImageList(query: String) = viewModelScope.launch {
        repository.getImageList(query).collect() {
            _imageList.value = it
        }
    }
}