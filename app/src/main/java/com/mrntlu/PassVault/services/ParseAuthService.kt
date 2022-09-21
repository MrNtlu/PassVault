package com.mrntlu.PassVault.services

import com.mrntlu.PassVault.models.UserRegister

interface ParseAuthService {

    fun parseLogin(username: String, password: String)
    fun parseRegister(userRegister: UserRegister)
    fun parseSignout()
}