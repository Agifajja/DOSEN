package com.example.dosena.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dosena.data.SetoranViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSetoranScreen(
    viewModel: SetoranViewModel,
    token: String,
    nim: String,
    setoranId: String? = null,
    onSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    var surat by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val detailLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(setoranId, viewModel.setoranResponse.value) {
        if (setoranId != null && !detailLoaded.value) {
            val detail = viewModel.getSetoranDetailById(setoranId)
            detail?.let {
                surat = it.nama
                label = it.label
                tanggal = it.info_setoran?.tgl_setoran ?: ""
                detailLoaded.value = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (setoranId == null) "Tambah Setoran" else "Edit Setoran") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = surat,
                onValueChange = { surat = it },
                label = { Text("Nama Surat") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Label") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tanggal,
                onValueChange = {},
                label = { Text("Tanggal Setoran") },
                placeholder = { Text("yyyy-MM-dd") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal")
                    }
                }
            )

            if (errorMessage != null) {
                Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (surat.isBlank() || label.isBlank() || tanggal.isBlank()) {
                            errorMessage = "Semua field harus diisi."
                            return@Button
                        }

                        errorMessage = null

                        if (setoranId == null) {
                            viewModel.tambahSetoran(token, nim, surat, label, tanggal) {
                                onSuccess()
                            }
                        } else {
                            viewModel.editSetoran(token, setoranId, surat, label, tanggal) {
                                onSuccess()
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Simpan")
                }

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            tanggal = dateFormat.format(Date(millis))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Batal")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
   