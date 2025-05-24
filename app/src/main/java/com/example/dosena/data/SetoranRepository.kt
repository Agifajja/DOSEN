package com.example.dosena.data

import retrofit2.Response

class SetoranRepository(private val apiService: ApiService) {

    suspend fun getSetoranMahasiswa(token: String, nim: String): Response<SetoranMahasiswaResponse> {
        return apiService.getSetoranMahasiswa("Bearer $token", nim)
    }

    suspend fun simpanSetoran(
        token: String,
        nim: String,
        request: SimpanSetoranRequest
    ): Response<GeneralResponse> {
        return apiService.simpanSetoranMahasiswa(token, nim, request)
    }

    suspend fun hapusSetoran(
        token: String,
        nim: String,
        request: DeleteSetoranRequest
    ): Response<GeneralResponse> {
        return apiService.deleteSetoranMahasiswa(token, nim, request)
    }

    suspend fun tambahSetoran(
        token: String,
        nim: String,
        nama: String,
        label: String,
        tanggal: String
    ): Response<GeneralResponse> {
        val request = TambahEditSetoranRequest(nama, label, tanggal)
        return apiService.tambahSetoranMahasiswa(token, nim, request)
    }

    suspend fun editSetoran(
        token: String,
        id: String,
        nama: String,
        label: String,
        tanggal: String
    ): Response<GeneralResponse> {
        val request = TambahEditSetoranRequest(nama, label, tanggal)
        return apiService.editSetoranMahasiswa("Bearer $token", id, request)
    }
}
