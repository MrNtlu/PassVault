package com.mrntlu.PassVault.models

data class PasswordItem(
    val username: String,
    val title: String,
    val note: String?,
    val password: String,
    var isEncrypted: Boolean?,
)
