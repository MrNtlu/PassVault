package com.mrntlu.PassVault.services

import com.mrntlu.PassVault.utils.Response
import com.parse.ParseObject
import kotlinx.coroutines.flow.Flow

interface ParseService {

    fun deletePassword(parseObject: ParseObject): Flow<Response<Boolean>>
    fun editPassword(parseObject: ParseObject, title: String, username: String, password: String, note: String?, isEncrypted: Boolean): Flow<Response<ParseObject>>
    fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean): Flow<Response<ParseObject>>
    fun getPasswords(): Flow<Response<ArrayList<ParseObject>>>
}