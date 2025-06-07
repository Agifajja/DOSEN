package com.example.dosena.data

import retrofit2.Response

class SetoranRepository(private val apiService: ApiService) {

    // Fungsi untuk mendapatkan data setoran mahasiswa
    suspend fun getSetoranMahasiswa(
        token: String,
        nim: String
    ): Response<SetoranMahasiswaResponse> {
        return apiService.getSetoranMahasiswa("Bearer $token", nim)
    }

    // Fungsi untuk mengedit data setoran mahasiswa
    suspend fun editSetoranMahasiswa(
        token: String,
        nim: String,
        request: SimpanSetoranRequest
    ): Response<GeneralResponse> {
        return apiService.editSetoranMahasiswa(
            token = "Bearer $token",
            nim = nim,
            request = request
        )
    }

    // Fungsi untuk menghapus data setoran mahasiswa
    suspend fun hapusSetoranMahasiswa(
        token: String,
        nim: String,
        request: HapusSetoranRequest
    ): Response<GeneralResponse> {
        return apiService.deleteSetoranMahasiswa(
            token = "Bearer $token",
            nim = nim,
            requestBody = request
        )
    }
}
