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

    // Fungsi untuk mengambil data setoran mahasiswa
    fun fetchSetoranMahasiswa(token: String, nim: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getSetoranMahasiswa(token, nim)
                if (response.isSuccessful) {
                    setoranResponse.value = response.body()
                    Log.d("SetoranVM", "Berhasil memuat data setoran.")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SetoranVM", "Gagal memuat data setoran: ${response.code()} - $errorBody")
                    responseMessage.value = "Gagal memuat data setoran: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("SetoranVM", "Error saat fetchSetoranMahasiswa: ${e.localizedMessage}")
                responseMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }


    // Fungsi untuk mendapatkan detail setoran berdasarkan ID
    fun getSetoranDetailById(id: String): SetoranDetail? {
        return setoranResponse.value?.data?.setoran?.detail?.find { it.id == id }
    }

    // Fungsi untuk menghapus data setoran mahasiswa
    fun hapusSetoran(
        token: String,
        nim: String,
        data: List<HapusKomponen>,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val request = HapusSetoranRequest(data)
                Log.d("SetoranVM", "Menghapus setoran dengan request: $request")

                val response = repository.hapusSetoranMahasiswa(token, nim, request)
                if (response.isSuccessful) {
                    responseMessage.value = response.body()?.message ?: "Setoran berhasil dihapus."
                    fetchSetoranMahasiswa(token, nim) // Refresh data setelah berhasil
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    responseMessage.value =
                        "Gagal menghapus setoran: ${response.message()} - $errorBody"
                    Log.e("SetoranVM", responseMessage.value)
                }
            } catch (e: Exception) {
                responseMessage.value = "Error saat menghapus setoran: ${e.localizedMessage}"
                Log.e("SetoranVM", responseMessage.value)
            } finally {
                isLoading.value = false
            }
        }
    }


    // Fungsi untuk mengedit data setoran mahasiswa
    fun editSetoran(
        token: String,
        nim: String,
        dataSetoran: List<KomponenSetoranRequest>,
        tanggal: String? = null,
        onDone: () -> Unit = {}
    ) {
        if (dataSetoran.isEmpty()) {
            responseMessage.value = "Data setoran tidak boleh kosong."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                val request = SimpanSetoranRequest(
                    data_setoran = dataSetoran,
                    tgl_setoran = tanggal // Optional
                )
                Log.d("SetoranVM", "Mengedit setoran dengan request: $request")

                val response = repository.editSetoranMahasiswa(token, nim, request)
                if (response.isSuccessful) {
                    responseMessage.value = "Setoran berhasil diperbarui."
                    fetchSetoranMahasiswa(token, nim) // Refresh data setelah berhasil
                } else {
                    val errorBody = response.errorBody()?.string()
                    responseMessage.value =
                        "Gagal memperbarui setoran: ${response.message()} - $errorBody"
                    Log.e("SetoranVM", responseMessage.value)
                }
            } catch (e: Exception) {
                responseMessage.value = "Error saat memperbarui setoran: ${e.localizedMessage}"
                Log.e("SetoranVM", responseMessage.value)
            } finally {
                isLoading.value = false
                onDone()
            }
        }
    }

}