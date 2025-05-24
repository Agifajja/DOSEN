package com.example.dosena.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SetoranViewModel(
    private val repository: SetoranRepository
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val setoranResponse = mutableStateOf<SetoranMahasiswaResponse?>(null)
    val responseMessage = mutableStateOf("")

    fun fetchSetoranMahasiswa(token: String, nim: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getSetoranMahasiswa(token, nim)
                if (response.isSuccessful) {
                    setoranResponse.value = response.body()
                    Log.d("SetoranVM", "Berhasil memuat data setoran.")
                } else {
                    Log.e("SetoranVM", "Gagal memuat: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SetoranVM", "Error: ${e.localizedMessage}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun simpanSetoran(token: String, nim: String, data: List<KomponenSetoran>, tgl: String?) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val request = SimpanSetoranRequest(data, tgl)
                val response = repository.simpanSetoran(token, nim, request)
                if (response.isSuccessful) {
                    responseMessage.value = response.body()?.message ?: "Setoran berhasil disimpan."
                    fetchSetoranMahasiswa(token, nim)
                } else {
                    responseMessage.value = "Gagal menyimpan: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                responseMessage.value = "Error saat simpan: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun hapusSetoran(token: String, nim: String, data: List<DeleteSetoranItem>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val request = DeleteSetoranRequest(data)
                val response = repository.hapusSetoran(token, nim, request)
                if (response.isSuccessful) {
                    responseMessage.value = response.body()?.message ?: "Setoran berhasil dihapus."
                    fetchSetoranMahasiswa(token, nim)
                    onSuccess()
                } else {
                    responseMessage.value = "Gagal menghapus: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                responseMessage.value = "Error saat hapus: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getSetoranDetailById(id: String): SetoranDetail? {
        return setoranResponse.value?.data?.setoran?.detail?.find { it.id == id }
    }

    fun tambahSetoran(token: String, nim: String, nama: String, label: String, tanggal: String, onDone: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.tambahSetoran(token, nim, nama, label, tanggal)
                if (response.isSuccessful) {
                    responseMessage.value = "Setoran berhasil ditambahkan."
                    fetchSetoranMahasiswa(token, nim)
                } else {
                    responseMessage.value = "Gagal tambah setoran: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                responseMessage.value = "Error tambah setoran: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
                onDone()
            }
        }
    }

    fun editSetoran(token: String, id: String, nama: String, label: String, tanggal: String, onDone: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.editSetoran(token, id, nama, label, tanggal)
                if (response.isSuccessful) {
                    responseMessage.value = "Setoran berhasil diperbarui."
                    val nim = setoranResponse.value?.data?.info?.nim
                    if (nim != null) {
                        fetchSetoranMahasiswa(token, nim)
                    }
                } else {
                    responseMessage.value = "Gagal edit setoran: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                responseMessage.value = "Error edit setoran: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
                onDone()
            }
        }
    }
}
