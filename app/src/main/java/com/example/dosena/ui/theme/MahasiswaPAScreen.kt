package com.example.dosena.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dosena.data.Mahasiswa
import com.example.dosena.data.MahasiswaPAViewModel

@Composable
fun MahasiswaPAScreen(
    viewModel: MahasiswaPAViewModel,
    token: String, // Token dari login
    navController: NavController // NavController buat navigate ke setoran
) {
    val mahasiswaList by viewModel.mahasiswaList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Panggil API saat pertama kali
    LaunchedEffect(Unit) {
        viewModel.getMahasiswaPA(token)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Daftar Mahasiswa Bimbingan", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn {
                items(mahasiswaList) { mhs ->
                    MahasiswaCard(mhs, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(mhs: Mahasiswa, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nama: ${mhs.nama}", style = MaterialTheme.typography.titleMedium)
            Text("NIM: ${mhs.nim}")
            Text("Angkatan: ${mhs.angkatan}")
            Text("Semester: ${mhs.semester}")
            Text("Progres Setoran: ${mhs.infoSetoran?.persentaseProgresSetor ?: 0.0} %")
            Text("Terakhir Setor: ${mhs.infoSetoran?.terakhirSetor ?: "Belum ada"}")

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol navigasi ke halaman setoran mahasiswa
            Button(onClick = {
                navController.navigate("setoran/${mhs.nim}")
            }) {
                Text("Lihat Setoran")
            }
        }
    }
}
