package com.example.dosena.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _loginError.value = null
            try {
                val result = repo.login(username, password)
                if (result != null) {
                    _loginState.value = true
                } else {
                    _loginError.value = "Login gagal. Periksa kembali email dan password."
                }
            } catch (e: Exception) {
                _loginError.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // ✅ Fungsi yang kamu butuhkan
    suspend fun getAccessToken(): String? {
        return repo.tokenPrefs.getAccessToken()
    }

    fun clearError() {
        _loginError.value = null
    }
}
