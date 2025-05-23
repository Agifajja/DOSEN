package com.example.dosena.data

import com.example.dosena.data.ApiService
import com.example.dosena.data.SetoranMahasiswaResponse

import retrofit2.Response

class SetoranRepository(private val apiService: ApiService) {

    suspend fun getSetoranMahasiswa(token: String, nim: String): Response<SetoranMahasiswaResponse> {
        return apiService.getSetoranMahasiswa("Bearer $token", nim)
    }
    suspend fun simpanSetoran(token: String, nim: String, request: SimpanSetoranRequest): Response<GeneralResponse> {
        return apiService.simpanSetoranMahasiswa(token, nim, request)
    }

    suspend fun hapusSetoran(token: String, nim: String, request: DeleteSetoranRequest): Response<GeneralResponse> {
        return apiService.deleteSetoranMahasiswa(token, nim, request)
    }
}
