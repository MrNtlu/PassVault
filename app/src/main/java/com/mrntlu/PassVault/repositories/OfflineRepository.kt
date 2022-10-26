package com.mrntlu.PassVault.repositories

import com.mrntlu.PassVault.Offline.Models.AccountsObject
import com.mrntlu.PassVault.Offline.Models.MailObject
import com.mrntlu.PassVault.Offline.Models.OthersObject
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.utils.Constants
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class OfflineRepository {

    private val realm: Realm

    init {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                OfflinePassword::class,
                MailObject::class,
                AccountsObject::class,
                OthersObject::class
            )
        )
            .name(Constants.RealmName)
            .build()
        realm = Realm.open(config)
    }

    suspend fun startMigration() {
        val migrationList = arrayListOf<OfflinePassword>()

        val mails = realm.query<MailObject>().find().map {
            OfflinePassword(
                idMail = it.mail,
                description = "Migrated from Mails",
                password = it.password
            )
        }

        val others = realm.query<OthersObject>().find().map {
            OfflinePassword(
                idMail = "",
                description = it.description,
                password = it.password
            )
        }

        val users = realm.query<AccountsObject>().find().map {
            OfflinePassword(
                idMail = it.idMail,
                description = it.description,
                password = it.password
            )
        }

        migrationList.apply {
            addAll(mails)
            addAll(others)
            addAll(users)
        }

        realm.write {
            migrationList.forEach { item ->
                this.copyToRealm(item)
            }

            delete(query<MailObject>().find())
            delete(query<OthersObject>().find())
            delete(query<AccountsObject>().find())
        }
    }

    fun getAllPasswords() = realm.query<OfflinePassword>().find()

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

    fun onCleared() {
        realm.close()
    }
}