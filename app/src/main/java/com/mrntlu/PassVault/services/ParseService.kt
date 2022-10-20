package com.mrntlu.PassVault.services

import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.Response
import com.parse.ParseObject
import kotlinx.coroutines.flow.Flow

interface ParseService {

    fun addPassword(title: String, username: String, password: String, note: String): Flow<Response<ParseObject>>
    fun getPasswords(): Flow<Response<List<PasswordItem>>>
    fun searchPasswords(): Flow<Response<List<ParseObject>>>
}