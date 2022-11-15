package com.mrntlu.PassVault.services

import com.mrntlu.PassVault.models.ImageListItem
import com.mrntlu.PassVault.utils.Constants.ApiEndPoint
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApiService {

    @GET(ApiEndPoint)
    suspend fun getImageList(
        @Query("query") query: String,
    ): List<ImageListItem>
}