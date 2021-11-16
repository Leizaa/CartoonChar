package com.example.cartoonchar.network.login

import android.content.Context
import com.example.cartoonchar.network.LoginService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject
import android.util.Base64
import retrofit2.HttpException
import java.io.IOException

class LoginRepository @Inject constructor(
    @ApplicationContext
    val context: Context,
    private val service: LoginService
) {

    private fun encryptToken(token: String): String {
        val plaintext: ByteArray = token.toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext = cipher.doFinal(plaintext)
        return Base64.encodeToString(ciphertext, Base64.DEFAULT)
    }

    suspend fun login(email: String, password: String): String? {
        try {
            val response = service.login("454041184B0240FBA3AACD15A1F7A8BB", email, password)
            if (!response.status) {
                throw Exception(response.message)
            }
            val sharedPreferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("TOKEN", encryptToken(response.token!!))
            }.apply()
            return response.token
        } catch (ex: HttpException) {
            if (ex.code() == 406) {
                throw Exception("Wrong email or password")
            }
            throw Exception("Internet error")
        } catch (ex: IOException) {
            throw Exception("Error parsing")
        }
    }
}