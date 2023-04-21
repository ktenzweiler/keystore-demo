package com.example.aessymmetriccrypto

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
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
}