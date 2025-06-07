package com.example.dosena.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dosena.data.SetoranViewModel
import com.example.dosena.data.HapusKomponen
import com.example.dosena.data.KomponenSetoranRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetoranScreen(
    viewModel: SetoranViewModel,
    nim: String,
    token: String,
    navController: NavController // Opsional: Gunakan sesuai kebutuhan
) {
    val isLoading by viewModel.isLoading
    val setoran by viewModel.setoranResponse
    val responseMessage by viewModel.responseMessage

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Ringkasan", "Log", "Detail")

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(responseMessage) {
        if (responseMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = responseMessage,
                actionLabel = "OK"
            )
            viewModel.responseMessage.value = "" // Reset pesan setelah ditampilkan
        }
    }

    LaunchedEffect(nim) {
        viewModel.fetchSetoranMahasiswa(token = token, nim = nim)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Setoran") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                setoran?.let { data ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(title) }
                                )
                            }
                        }

                        when (selectedTabIndex) {
                            2 -> {
                                // Detail Setoran
                                LazyColumn {
                                    items(data.data.setoran.detail) { detail ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (detail.sudah_setor)
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text("Surat: ${detail.nama} (${detail.nama_arab})")
                                                Text("Status: ${if (detail.sudah_setor) "✅ Sudah Setor" else "❌ Belum Setor"}")
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    OutlinedButton(
                                                        onClick = {
                                                            val dataToDelete = listOf(
                                                                HapusKomponen(
                                                                    id = detail.id,
                                                                    id_komponen_setoran = detail.external_id,
                                                                    nama_komponen_setoran = detail.nama
                                                                )
                                                            )
                                                            viewModel.hapusSetoran(
                                                                token = token,
                                                                nim = nim,
                                                                data = dataToDelete,
                                                                onSuccess = {
                                                                    // Handle setelah penghapusan berhasil
                                                                }
                                                            )
                                                        }
                                                    ) {
                                                        Text("Hapus")
                                                    }

                                                    Button(
                                                        onClick = {
                                                            viewModel.editSetoran(
                                                                token = token,
                                                                nim = nim,
                                                                dataSetoran = listOf(
                                                                    KomponenSetoranRequest(
                                                                        nama_komponen_setoran = detail.nama,
                                                                        id_komponen_setoran = detail.external_id
                                                                    )
                                                                ),
                                                                tanggal = "2025-06-07", // Contoh tanggal
                                                                onDone = {
                                                                    // Optional: Tindakan setelah selesai edit
                                                                }
                                                            )
                                                        }
                                                    ) {
                                                        Text("Edit")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } ?: Text("Data tidak ditemukan.")
            }
        }
    }
}
