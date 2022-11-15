package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.models.ImageListItem
import com.mrntlu.PassVault.services.ImageApiService
import com.mrntlu.PassVault.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageListRepository @Inject constructor(
    private val imageApiService: ImageApiService,
) {
    suspend fun getImageList(query: String): Flow<Response<List<ImageListItem>>> = flow {
        try {
            emit(Response.Loading)

            emit(Response.Success(imageApiService.getImageList(query)))
//                .catch { err ->
//                emit(Response.Failure(err.message ?: err.toString()))
//            }.collect() {
//                emit(Response.Success(it))
//            }
        } catch (error: Exception) {
            emit(Response.Failure(error.message ?: error.toString()))
        }
    }.flowOn(Dispatchers.IO)
}