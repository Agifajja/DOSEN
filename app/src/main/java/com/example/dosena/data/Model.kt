package com.example.dosena.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("expires_in")
    val expiresIn: Int
)

data class PaSayaResponse(
    val data: DosenPAData
)

data class DosenPAData(
    val nama: String,
    val email: String,

    @SerializedName("info_mahasiswa_pa")
    val infoMahasiswaPA: MahasiswaPAInfo
)

data class MahasiswaPAInfo(
    @SerializedName("daftar_mahasiswa")
    val daftarMahasiswa: List<Mahasiswa>
)

data class Mahasiswa(
    val nama: String,
    val nim: String,
    val angkatan: String,
    val info_setoran: SetoranInfo? // ⬅️ Bisa null
)


data class SetoranInfo(
    @SerializedName("total_sudah_setor")
    val totalSudahSetor: Int,

    @SerializedName("total_belum_setor")
    val totalBelumSetor: Int,

    @SerializedName("persentase_progres_setor")
    val persentaseProgresSetor: Double,

    @SerializedName("terakhir_setor")
    val terakhirSetor: String?
)
