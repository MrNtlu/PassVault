package com.mrntlu.PassVault.utils

import android.os.Build
import android.util.Base64
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Cryptography (key: String) {
    private val key = SecretKeySpec(key.toByteArray(), "AES")
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val iv = IvParameterSpec(ByteArray(16))

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cipher.doFinal(getDecoder().decode(cipherText))
        } else {
            cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))
        }

        return String(plainText)
    }

    fun encrypt(inputText: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(inputText.toByteArray())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getEncoder().encodeToString(cipherText)
        } else {
            Base64.encodeToString(cipherText, Base64.DEFAULT)
        }
    }
}