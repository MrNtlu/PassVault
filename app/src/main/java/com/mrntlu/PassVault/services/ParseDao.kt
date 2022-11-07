package com.mrntlu.PassVault.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mrntlu.PassVault.models.PasswordItem

@Dao
interface ParseDao {
    @Query("Select * From account")
    fun getPasswords(): List<PasswordItem>

    @Insert
    fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean)

    @Query("Delete From account")
    fun deletePasswords()
}