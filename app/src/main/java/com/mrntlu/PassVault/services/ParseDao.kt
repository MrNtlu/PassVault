package com.mrntlu.PassVault.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrntlu.PassVault.models.PasswordItem

@Dao
interface ParseDao {
    @Query("Select * From account")
    fun getPasswords(): MutableList<PasswordItem>

    @Query("Select * From account Order By " +
            "Case When :sort = 'title' and :isDescending Then title End COLLATE NOCASE Desc," +
            "Case When :sort = 'title' and :isDescending = 0 Then title End COLLATE NOCASE Asc," +
            "Case When :sort = 'username' and :isDescending Then username End COLLATE NOCASE Desc," +
            "Case When :sort = 'username' and :isDescending = 0 Then username End COLLATE NOCASE Asc",
    )
    fun getPasswordsSorted(sort: String, isDescending: Boolean): MutableList<PasswordItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPasswords(passwordList: List<PasswordItem>)

    @Query("Delete From account")
    suspend fun deletePasswords()
}