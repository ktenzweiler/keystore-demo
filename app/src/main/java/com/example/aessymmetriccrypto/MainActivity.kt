package com.example.aessymmetriccrypto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aessymmetriccrypto.ui.theme.AESSymmetricCryptoTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MAIN-ACTIVITY-TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AESSymmetricCryptoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }

                val alias = "secret_key"
                val plainText = "I am some plain text that needs to be encrypted"

                val cryptoUtils = CryptoUtils()
                cryptoUtils.generateSecretKey(alias)

                val cipherText = cryptoUtils.encrypt(alias, plainText = plainText)

                Log.d(TAG, "onCreate: ciphertext = $cipherText")

                cipherText?.let {
                    val clearText = cryptoUtils.decrypt(alias, it)
                    Log.d(TAG, "onCreate: cleartext decrypted = $clearText")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AESSymmetricCryptoTheme {
        Greeting("Android")
    }
}