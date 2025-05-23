package com.example.dosena.ui.theme

import androidx.browser.trusted.Token
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dosena.data.SetoranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetoranScreen(
    viewModel: SetoranViewModel,
    nim: String,
    token: String
) {
    val isLoading by viewModel.isLoading
    val setoran by viewModel.setoranResponse

    LaunchedEffect(nim) {
        viewModel.fetchSetoranMahasiswa(token = token, nim = nim)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Setoran") }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                setoran?.let { data ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header Mahasiswa
                        Text("Nama: ${data.data.info.nama}", style = MaterialTheme.typography.titleMedium)
                        Text("Dosen PA: ${data.data.info.dosen_pa.nama}")
                        Spacer(modifier = Modifier.height(16.dp))

                        // Ringkasan Setoran
                        Text("Ringkasan Setoran", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 250.dp)
                        ) {
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

                        Spacer(modifier = Modifier.height(16.dp))

                        // Log Riwayat Setoran
                        Text("Riwayat Log Setoran", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            items(data.data.setoran.log) { log ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Keterangan: ${log.keterangan}")
                                        Text("Aksi: ${log.aksi}")
                                        Text("Waktu: ${log.timestamp}")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detail Surat Setoran
                        Text("Detail Surat Setoran", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

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
