package com.example.dosena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dosena.data.*
import com.example.dosena.ui.LoginScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            val tokenPrefs = TokenPreferences(context)
            val authApi = RetrofitClient.authApi
            val mainApi = RetrofitClient.mainApi
            val repo = AuthRepository(authApi, mainApi, tokenPrefs)

            // Buat factory untuk LoginViewModel
            val loginViewModel = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(repo) as T
                }
            }.let {
                ViewModelProvider(this, it).get(LoginViewModel::class.java)
            }

            // Buat factory untuk MahasiswaPAViewModel (tanpa api)
            val mahasiswaViewModel = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MahasiswaPAViewModel(repo) as T
                }
            }.let {
                ViewModelProvider(this, it).get(MahasiswaPAViewModel::class.java)
            }

            // Tampilan utama
            MaterialTheme {
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(viewModel = loginViewModel) {
                            navController.navigate("mahasiswa-pa") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    }
                }
            }
        }
    }

