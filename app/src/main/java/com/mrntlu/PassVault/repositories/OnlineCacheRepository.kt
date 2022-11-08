package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.services.ParseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OnlineCacheRepository @Inject constructor(
    private val parseDao: ParseDao,
) {

    fun getPasswords() = parseDao.getPasswords()

    suspend fun addPassword(title: String, username: String, password: String, note: String?, isEncrypted: Boolean) = withContext(Dispatchers.IO) {
        parseDao.addPassword(title, username, password, note, isEncrypted)
    }
}