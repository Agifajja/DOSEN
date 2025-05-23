package com.example.dosena.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenPreferences(private val context: Context) {
    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_IN = longPreferencesKey("expires_in")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Long) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
            it[EXPIRES_IN] = System.currentTimeMillis() + (expiresIn * 900)
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }
    val tokenExpiry: Flow<Long?> = context.dataStore.data.map { it[EXPIRES_IN] }

    // ✅ Tambahkan fungsi ini:
    suspend fun getAccessToken(): String? {
        return accessToken.first()
    }
}
