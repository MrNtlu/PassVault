package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.utils.Constants
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject

class OfflineRepository {

    private val realm: Realm

    init {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                OfflinePassword::class,
            )
        )
            .name(Constants.RealmName)
            .build()
        realm = Realm.open(config)
    }

    fun getAllPasswords() = realm.query<OfflinePassword>().find()

    fun searchPassword(text: String) = realm.query<OfflinePassword>("idMail CONTAINS[c] $0 OR description CONTAINS[c] $0", text).find()

    suspend fun addPassword(idMail: String, description: String, password: String) {
        realm.write {
            this.copyToRealm(
                OfflinePassword(
                    idMail = idMail,
                    description = description,
                    password = password
                )
            )
        }
    }

    suspend fun editPassword(item: OfflinePassword, idMail: String, description: String, password: String) {
        realm.write {
            val editObject = findLatest(item)
            editObject?.let {
                it.idMail = idMail
                it.description = description
                it.password = password
            }
        }
    }

    suspend fun deletePassword(realmObject: RealmObject) {
        realm.write {
            val deleteObject = findLatest(realmObject)
            deleteObject?.let {
                delete(it)
            }
        }
    }

    suspend fun deleteAllPasswords() {
        realm.write {
            val passwords = this.query<OfflinePassword>().find()
            delete(passwords)
        }
    }

    fun onCleared() {
        realm.close()
    }
}