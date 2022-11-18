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
    @ColumnInfo(name = "parse_id") val parseID: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null,
    @ColumnInfo(name = "image_color") val imageColor: String,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
) {
    constructor(
        username: String, title: String, note: String?,
        password: String, imageColor: String, isEncrypted: Boolean?
    ): this(
        username, title, note, password, isEncrypted, "", null, imageColor,
    )
}
