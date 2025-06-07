package com.example.dosena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.dosena.data.*
import com.example.dosena.ui.LoginScreen
import com.example.dosena.ui.MahasiswaPAScreen
import com.example.dosena.ui.theme.SetoranScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            // Inisialisasi API & Repository
            val tokenPrefs = TokenPreferences(context)
            val authApi = RetrofitClient.authApi
            val mainApi = RetrofitClient.mainApi

            val authRepository = AuthRepository(authApi, mainApi, tokenPrefs)
            val setoranRepository = SetoranRepository(mainApi)

            // ViewModel Factory
            val viewModelFactory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return when (modelClass) {
                        LoginViewModel::class.java -> LoginViewModel(authRepository) as T
                        MahasiswaPAViewModel::class.java -> MahasiswaPAViewModel(authRepository) as T
                        SetoranViewModel::class.java -> SetoranViewModel(setoranRepository) as T
                        else -> throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            }

            // ViewModels
            val loginViewModel = remember { ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java] }
            val mahasiswaViewModel = remember { ViewModelProvider(this, viewModelFactory)[MahasiswaPAViewModel::class.java] }
            val setoranViewModel = remember { ViewModelProvider(this, viewModelFactory)[SetoranViewModel::class.java] }

            // Token penyimpanan sementara
            var token by remember { mutableStateOf("") }

            MaterialTheme {
                NavHost(navController = navController, startDestination = "login") {

                    // Login
                    composable("login") {
                        LoginScreen(viewModel = loginViewModel) { accessToken ->
                            token = accessToken
                            navController.navigate("mahasiswa-pa") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }

                    // Mahasiswa PA
                    composable("mahasiswa-pa") {
                        MahasiswaPAScreen(
                            viewModel = mahasiswaViewModel,
                            token = token,
                            navController = navController
                        )
                    }

                    // Setoran Mahasiswa
                    composable("setoran/{nim}") { backStackEntry ->
                        val nim = backStackEntry.arguments?.getString("nim") ?: ""
                        SetoranScreen(
                            viewModel = setoranViewModel,
                            nim = nim,
                            token = token,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
