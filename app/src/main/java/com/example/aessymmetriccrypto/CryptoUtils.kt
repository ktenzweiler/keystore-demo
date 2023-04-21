package com.example.aessymmetriccrypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.util.Log
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

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
        val protectionParameter = KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setKeyValidityStart(start.time)
            .setKeyValidityEnd(end.time)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()

        ks.setEntry(alias, entry, protectionParameter)
    }

    fun generateSecretMasterKey(alias: String, context: Context) {
        try {
            val start: Calendar = Calendar.getInstance()
            val end: Calendar = Calendar.getInstance()
            end.add(Calendar.YEAR, 2)

            val keySpec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setKeyValidityStart(start.time)
                .setKeyValidityEnd(end.time)
                .build()

            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .setKeyGenParameterSpec(keySpec)
                .build()
        } catch (e: Exception) {
            Log.e(TAG, "generateSecretMasterKey: ERROR = ${e.message} ")
        }
    }
}