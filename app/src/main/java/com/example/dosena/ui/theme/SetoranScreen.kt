package com.example.dosena.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dosena.data.SetoranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetoranScreen(
    viewModel: SetoranViewModel,
    nim: String,
    token: String,
    navController: NavController,
    onAddClick: () -> Unit = {},
    onEditClick: (id: String) -> Unit = {},
    onDeleteClick: (id: String) -> Unit = {}
) {
    val isLoading by viewModel.isLoading
    val setoran by viewModel.setoranResponse

    var showDeleteDialog by remember { mutableStateOf(false) }
    var idToDelete by remember { mutableStateOf<String?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabTitles = listOf("Ringkasan", "Log", "Detail")

    LaunchedEffect(nim) {
        viewModel.fetchSetoranMahasiswa(token = token, nim = nim)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Setoran") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                setoran?.let { data ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nama: ${data.data.info.nama}", style = MaterialTheme.typography.titleMedium)
                        Text("Dosen PA: ${data.data.info.dosen_pa.nama}")
                        Spacer(modifier = Modifier.height(16.dp))

                        // TAB
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(title) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        when (selectedTabIndex) {
                            0 -> {
                                // Ringkasan Setoran
                                LazyColumn {
                                    items(data.data.setoran.ringkasan) { ringkasan ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text("Label: ${ringkasan.label}")
                                                Text("Wajib Setor: ${ringkasan.total_wajib_setor}")
                                                Text("Sudah Setor: ${ringkasan.total_sudah_setor}")
                                                Text("Belum Setor: ${ringkasan.total_belum_setor}")
                                                Text("Progress: ${ringkasan.persentase_progres_setor}%")
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                // Log Setoran
                                
                            }

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
                                                Text("Label: ${detail.label}")
                                                Text("Status: ${if (detail.sudah_setor) "✅ Sudah Setor" else "❌ Belum Setor"}")

                                                detail.info_setoran?.let {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text("Tgl Setoran: ${it.tgl_setoran}")
                                                    Text("Tgl Validasi: ${it.tgl_validasi}")
                                                    Text("Dosen Pengesah: ${it.dosen_yang_mengesahkan.nama}")
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.End,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Button(
                                                        onClick = { onEditClick(detail.id) },
                                                        modifier = Modifier.padding(end = 8.dp)
                                                    ) {
                                                        Text("Edit")
                                                    }
                                                    OutlinedButton(
                                                        onClick = {
                                                            idToDelete = detail.id
                                                            showDeleteDialog = true
                                                        }
                                                    ) {
                                                        Text("Hapus")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onAddClick,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Tambah Setoran")
                                }
                            }
                        }
                    }
                } ?: Text("Data tidak ditemukan.")
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Hapus Setoran") },
                text = { Text("Yakin ingin menghapus setoran ini?") },
                confirmButton = {
                    TextButton(onClick = {
                        idToDelete?.let { onDeleteClick(it) }
                        showDeleteDialog = false
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}
