package com.example.dosena.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repo.login(username, password)) {
                is AuthResult.Success -> {
                    _loginState.value = true
                    _loginError.value = null
                }
                is AuthResult.Failure -> {
                    _loginState.value = false
                    _loginError.value = result.message
                }
            }
            _loading.value = false
        }
    }
}
