package com.example.dosena.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MahasiswaPAViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _mahasiswaList = MutableStateFlow<List<Mahasiswa>>(emptyList())
    val mahasiswaList: StateFlow<List<Mahasiswa>> = _mahasiswaList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getMahasiswaPA(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repo.getMahasiswaPA("Bearer $token")
                if (response != null) {
                    _mahasiswaList.value = response.data.infoMahasiswaPA.daftarMahasiswa
                } else {
                    _error.value = "Gagal memuat data mahasiswa PA."
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

