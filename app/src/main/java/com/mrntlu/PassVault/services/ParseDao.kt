package com.mrntlu.PassVault.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrntlu.PassVault.models.PasswordItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ParseDao {
    @Query("Select * From account")
    fun getPasswords(): Flow<List<PasswordItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPasswords(passwordList: List<PasswordItem>)

    @Query("Delete From account")
    suspend fun deletePasswords()
}