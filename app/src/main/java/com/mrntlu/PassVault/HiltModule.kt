package com.mrntlu.PassVault

import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.repositories.OfflineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideHomeRepository(): HomeRepository = HomeRepository()

    @Provides
    fun provideOfflineRepository(): OfflineRepository = OfflineRepository()
}