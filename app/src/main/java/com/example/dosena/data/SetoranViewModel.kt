package com.example.dosena.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dosena.data.SetoranMahasiswaResponse
import kotlinx.coroutines.launch

class SetoranViewModel(private val repository: SetoranRepository) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val setoranResponse = mutableStateOf<SetoranMahasiswaResponse?>(null)

    fun fetchSetoranMahasiswa(token: String, nim: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.getSetoranMahasiswa(token, nim)
                if (response.isSuccessful) {
                    setoranResponse.value = response.body()
                    Log.d("SetoranVM", "Berhasil load data setoran.")
                } else {
                    Log.e("SetoranVM", "Gagal: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SetoranVM", "Error: ${e.localizedMessage}")
            } finally {
                isLoading.value = false
            }
        }
    }

}
