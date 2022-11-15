package com.mrntlu.PassVault

import android.content.Context
import androidx.room.Room
import com.mrntlu.PassVault.services.ImageApiService
import com.mrntlu.PassVault.services.ParseDao
import com.mrntlu.PassVault.services.ParseDatabase
import com.mrntlu.PassVault.utils.Constants
import com.mrntlu.PassVault.utils.Constants.BaseApiEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): ImageApiService =
        Retrofit.Builder()
            .baseUrl(BaseApiEndPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageApiService::class.java)

    @Singleton
    @Provides
    fun provideParseDatabase(@ApplicationContext context: Context): ParseDatabase =
        Room
            .databaseBuilder(context, ParseDatabase::class.java, Constants.CacheDatabaseName)
            .build()

    @Singleton
    @Provides
    fun provideParseDao(parseDatabase: ParseDatabase): ParseDao = parseDatabase.getParseDao()
}