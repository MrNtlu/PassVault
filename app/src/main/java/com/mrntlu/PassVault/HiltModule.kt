package com.mrntlu.PassVault

import android.content.Context
import androidx.room.Room
import com.mrntlu.PassVault.repositories.HomeRepository
import com.mrntlu.PassVault.repositories.OfflineRepository
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    //TODO https://github.com/nameisjayant/Dagger-hilt-with-RoomDatabase-and-Retrofit-in-Android/tree/master/app/src/main/java/com/example/roomwithretrofit
    @Provides
    @Singleton
    fun provideParseDatabase(@ApplicationContext context: Context): ParseDatabase = Room
        .databaseBuilder(context, ParseDatabase::class.java, "parseDatabase")
        .build()

    @Provides
    fun provideParseDao(parseDatabase: ParseDatabase): ParseDao = parseDatabase.getParseDao()

    @Provides
    fun provideHomeRepository(): HomeRepository = HomeRepository()

    @Provides
    fun provideOfflineRepository(): OfflineRepository = OfflineRepository()
}