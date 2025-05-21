package com.example.dosena.data

import android.util.Log
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val authApi: ApiService,
    private val mainApi: ApiService,
    private val tokenPrefs: TokenPreferences
) {
    suspend fun login(username: String, password: String): AuthResult {
        return try {
            val response = authApi.login(username = username, password = password)
            if (response.isSuccessful) {
                response.body()?.let {
                    tokenPrefs.saveTokens(it.accessToken, it.refreshToken, it.expiresIn.toLong())
                    AuthResult.Success
                } ?: AuthResult.Failure("Login gagal: response kosong.")
            } else {
                val error = response.errorBody()?.string()
                Log.e("LOGIN_FAILED", "Error: ${response.code()} - $error")
                AuthResult.Failure("Login gagal: $error")
            }
        } catch (e: Exception) {
            AuthResult.Failure("Terjadi kesalahan: ${e.message}")
        }
    }

    suspend fun getValidAccessToken(): String? {
        val expiry = tokenPrefs.tokenExpiry.first() ?: 0L
        val now = System.currentTimeMillis()
        val accessToken = tokenPrefs.accessToken.first()

        return if (now > expiry) {
            val refresh = tokenPrefs.refreshToken.first()
            val res = authApi.refreshToken(refreshToken = refresh ?: return null)
            if (res.isSuccessful) {
                val body = res.body()!!
                tokenPrefs.saveTokens(body.accessToken, body.refreshToken, body.expiresIn.toLong())
                body.accessToken
            } else {
                Log.e("TOKEN_REFRESH_FAILED", "Error: ${res.code()} - ${res.errorBody()?.string()}")
                null
            }
        } else accessToken
    }

    suspend fun getMahasiswaPA(): PaSayaResponse? {
        val token = getValidAccessToken() ?: return null
        val response = mainApi.getMahasiswaPA("Bearer $token")
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("PA_FETCH_FAILED", "Error: ${response.code()} - ${response.errorBody()?.string()}")
            null
        }
    }
}


sealed class AuthResult {
    object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

