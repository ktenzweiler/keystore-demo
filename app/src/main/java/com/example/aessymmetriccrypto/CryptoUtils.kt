package com.example.aessymmetriccrypto

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoUtils {

    private val TAG = "CRYPTO-UTILS-TAG"
    fun generateSecretKey(alias: String) {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)

        val secretKey: SecretKey = keyGen.generateKey()

        val start: Calendar = Calendar.getInstance()
        val end: Calendar = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)

        val entry = KeyStore.SecretKeyEntry(secretKey)
        val protectionParameter =
            KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setKeyValidityStart(start.time)
                .setKeyValidityEnd(end.time)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()

        ks.setEntry(alias, entry, protectionParameter)
    }

    fun encrypt(alias: String, plainText: String): String? {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val secretKey = ks.getKey(alias, null)

        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val cipherText = Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
            val iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT)

            "${cipherText}.$iv"
        } catch (e: Exception) {
            Log.e(TAG, "encrypt: error msg = ${e.message}")
            null
        }
    }

    fun decrypt(alias: String, cipherText: String): String? {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val secretKey = ks.getKey(alias, null)

        val array = cipherText.split(".")
        val cipherData = Base64.decode(array[0], Base64.DEFAULT)
        val iv = Base64.decode(array[1], Base64.DEFAULT)

        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            val spec = IvParameterSpec(iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val clearText = cipher.doFinal(cipherData)

            String(clearText, 0, clearText.size, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "decrypt: error msg = ${e.message}")
            null
        }
    }
}