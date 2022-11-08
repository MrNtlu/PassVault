package com.mrntlu.PassVault

import android.content.Context
import androidx.room.Room
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.repositories.OfflineRepository
import com.mrntlu.PassVault.repositories.OnlineCacheRepository
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import com.mrntlu.PassVault.utils.Constants.CacheDatabaseName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    @Singleton
    fun provideParseDatabase(@ApplicationContext context: Context): ParseDatabase =
        Room
            .databaseBuilder(context, ParseDatabase::class.java, CacheDatabaseName)
            .build()

    @Provides
    fun provideParseDao(parseDatabase: ParseDatabase): ParseDao = parseDatabase.getParseDao()

    @Provides
    fun provideOnlineCacheRepository(parseDao: ParseDao) = OnlineCacheRepository(parseDao)

    @Provides
    fun provideHomeRepository(parseDao: ParseDao, parseDatabase: ParseDatabase): HomeRepository = HomeRepository(parseDao, parseDatabase)

    @Provides
    fun provideOfflineRepository(): OfflineRepository = OfflineRepository()
}