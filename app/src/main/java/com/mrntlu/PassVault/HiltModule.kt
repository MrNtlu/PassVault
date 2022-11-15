package com.mrntlu.PassVault

import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.repositories.ImageListRepository
import com.mrntlu.PassVault.repositories.OfflineRepository
import com.mrntlu.PassVault.services.ImageApiService
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideImageListRepository(imageApiService: ImageApiService): ImageListRepository = ImageListRepository(imageApiService)

    @Provides
    fun provideHomeRepository(parseDao: ParseDao, parseDatabase: ParseDatabase): HomeRepository = HomeRepository(parseDao, parseDatabase)

    @Provides
    fun provideOfflineRepository(): OfflineRepository = OfflineRepository()
}