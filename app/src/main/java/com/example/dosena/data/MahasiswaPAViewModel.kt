package com.example.dosena.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahasiswaPAViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _mahasiswaList = MutableStateFlow<List<Mahasiswa>>(emptyList())
    val mahasiswaList = _mahasiswaList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        getMahasiswaPA()
    }

    fun getMahasiswaPA() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repo.getMahasiswaPA()
                if (response != null) {
                    val data = response.data.infoMahasiswaPA.daftarMahasiswa
                    _mahasiswaList.value = data
                } else {
                    _error.value = "Gagal memuat data mahasiswa PA."
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
