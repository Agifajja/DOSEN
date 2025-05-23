package com.example.dosena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.*
import com.example.dosena.data.*
import com.example.dosena.ui.LoginScreen
import com.example.dosena.ui.MahasiswaPAScreen
import com.example.dosena.ui.theme.SetoranScreen
import com.example.dosena.data.SetoranRepository


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
            val repo = AuthRepository(authApi, mainApi, tokenPrefs)

            // LoginViewModel
            val loginViewModel = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(repo) as T
                }
            }.let {
                ViewModelProvider(this, it).get(LoginViewModel::class.java)
            }

            // MahasiswaPAViewModel
            val mahasiswaViewModel = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MahasiswaPAViewModel(repo) as T
                }
            }.let {
                ViewModelProvider(this, it).get(MahasiswaPAViewModel::class.java)
            }

            // SetoranViewModel
            val setoranRepo = SetoranRepository(mainApi)
            val setoranViewModel = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SetoranViewModel(setoranRepo) as T
                }
            }.let {
                ViewModelProvider(this, it).get(SetoranViewModel::class.java)
            }

            // Tempat penyimpanan token sementara
            var token by remember { mutableStateOf("") }

            MaterialTheme {
                NavHost(navController, startDestination = "login") {

                    // Login Screen
                    composable("login") {
                        LoginScreen(viewModel = loginViewModel) { accessToken ->
                            token = accessToken
                            navController.navigate("mahasiswa-pa") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }

                    // Mahasiswa PA Screen
                    composable("mahasiswa-pa") {
                        MahasiswaPAScreen(
                            viewModel = mahasiswaViewModel,
                            token = token,
                            navController = navController // biar bisa navigate ke setoran
                        )
                    }

                    // Setoran Mahasiswa Screen
                    composable("setoran/{nim}") { backStackEntry ->
                        val nim = backStackEntry.arguments?.getString("nim") ?: ""
                        SetoranScreen(
                            viewModel = setoranViewModel,
                            nim = nim,
                            token = token // <-- tambahkan ini
                        )
                    }

                }
            }
        }
    }
}
