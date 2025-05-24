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
import com.example.dosena.ui.theme.FormSetoranScreen
import com.example.dosena.ui.theme.SetoranScreen
import androidx.navigation.navArgument


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

            // ViewModel: Login
            val loginViewModel = remember {
                ViewModelProvider(this, object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LoginViewModel(authRepository) as T
                    }
                })[LoginViewModel::class.java]
            }

            // ViewModel: Mahasiswa PA
            val mahasiswaViewModel = remember {
                ViewModelProvider(this, object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MahasiswaPAViewModel(authRepository) as T
                    }
                })[MahasiswaPAViewModel::class.java]
            }

            // ViewModel: Setoran
            val setoranViewModel = remember {
                ViewModelProvider(this, object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SetoranViewModel(setoranRepository) as T
                    }
                })[SetoranViewModel::class.java]
            }

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
                            navController = navController,
                            onAddClick = {
                                navController.navigate("form-setoran/$nim")
                            },
                            onEditClick = { id ->
                                navController.navigate("form-setoran/$nim?id=$id")
                            },
                            onDeleteClick = { id ->
                                val deleteItem = DeleteSetoranItem(
                                    id = id,
                                    idKomponenSetoran = "", // <-- Isi sesuai data real, atau kosong jika tidak wajib
                                    namaKomponenSetoran = "" // <-- Isi sesuai data real
                                )
                                setoranViewModel.hapusSetoran(
                                    token = token,
                                    nim = nim,
                                    data = listOf(deleteItem),
                                    onSuccess = {
                                        setoranViewModel.fetchSetoranMahasiswa(token, nim)
                                    }
                                )
                            }

                        )
                    }

                    // Form Tambah/Edit Setoran
                    composable(
                        "form-setoran/{nim}?id={id}",
                        arguments = listOf(
                            navArgument("nim") { type = NavType.StringType },
                            navArgument("id") {
                                type = NavType.StringType
                                nullable = true
                                 defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val nim = backStackEntry.arguments?.getString("nim") ?: ""
                        val id = backStackEntry.arguments?.getString("id")
                        FormSetoranScreen(
                            viewModel = setoranViewModel,
                            nim = nim,
                            token = token,
                            setoranId = id,
                            onSuccess = {
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
