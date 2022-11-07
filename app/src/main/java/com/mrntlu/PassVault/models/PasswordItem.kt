package com.mrntlu.PassVault.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class PasswordItem(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "is_encrypted") var isEncrypted: Boolean?,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
)
