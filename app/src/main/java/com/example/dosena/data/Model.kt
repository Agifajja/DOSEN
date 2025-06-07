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
    val ringkasan: List<Ringkasan>,
    @SerializedName("daftar_mahasiswa")
    val daftarMahasiswa: List<Mahasiswa>
)

data class Ringkasan(
    val tahun: String,
    val total: Int
)

data class Mahasiswa(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    @SerializedName("info_setoran")
    val infoSetoran: SetoranInfo?
)

data class SetoranInfo(
    @SerializedName("total_wajib_setor")
    val totalWajibSetor: Int,
    @SerializedName("total_sudah_setor")
    val totalSudahSetor: Int,
    @SerializedName("total_belum_setor")
    val totalBelumSetor: Int,
    @SerializedName("persentase_progres_setor")
    val persentaseProgresSetor: Double,
    @SerializedName("tgl_terakhir_setor")
    val tglTerakhirSetor: String?,
    @SerializedName("terakhir_setor")
    val terakhirSetor: String?
)
data class SetoranResponse(
    val info: InfoMahasiswa,
    val setoran: SetoranMahasiswa
)




data class SetoranMahasiswa(
    val detail: List<DetailSetoran>,
    val info_dasar: InfoDasarSetoran,
    val log: List<LogSetoran>,
    val ringkasan: List<RingkasanSetoran>
)


data class InfoDasarSetoran(
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double,
    val tanggal_terakhir_setor: String?
)

data class LogSetoran(
    val tanggal: String,
    val status: String,
    val keterangan: String
)



data class SetoranMahasiswaResponse(
    val response: Boolean,
    val message: String,
    val data: SetoranDataWrapper
)

data class SetoranDataWrapper(
    val info: MahasiswaInfo,
    val setoran: SetoranData
)

data class MahasiswaInfo(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    val dosen_pa: DosenPA
)



data class SetoranData(
    val log: List<SetoranLog>,
    val info_dasar: InfoDasar,
    val ringkasan: List<RingkasanSetoran>,
    val detail: List<SetoranDetail>
)





data class KomponenSetoran(
    @SerializedName("id_komponen_setoran")
    val idKomponenSetoran: String,
    @SerializedName("nama_komponen_setoran")
    val namaKomponenSetoran: String
)










data class GeneralResponse(
    val response: Boolean,
    val message: String
)



data class SetoranDetail(
    val id: String,
    val nama: String,
    val external_id: String,
    val nama_arab: String,
    val label: String,
    val sudah_setor: Boolean,
    val info_setoran: InfoSetoran?
)


data class DataSetoranMahasiswa(
    val info: InfoMahasiswa,
    val setoran: Setoran
)

data class InfoMahasiswa(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    val dosen_pa: DosenPA
)

data class DosenPA(
    val nip: String,
    val nama: String,
    val email: String
)

data class Setoran(
    val log: List<SetoranLog>,
    val info_dasar: InfoDasar,
    val ringkasan: List<RingkasanSetoran>,
    val detail: List<DetailSetoran>
)

data class SetoranLog(
    val id: Int,
    val keterangan: String,
    val aksi: String,
    val ip: String,
    val user_agent: String,
    val timestamp: String,
    val nim: String,
    val dosen_yang_mengesahkan: DosenPA
)

data class InfoDasar(
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double,
    val tgl_terakhir_setor: String,
    val terakhir_setor: String
)

data class RingkasanSetoran(
    val label: String,
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double
)

data class DetailSetoran(
    val id: String,
    val nama: String,
    val external_id: String,
    val nama_arab: String,
    val label: String,
    val sudah_setor: Boolean,
    val info_setoran: InfoSetoran?
)

data class InfoSetoran(
    val id: String,
    val tgl_setoran: String,
    val tgl_validasi: String,
    val dosen_yang_mengesahkan: DosenPA
)



data class SimpanSetoranRequest(
    val data_setoran: List<KomponenSetoranRequest>,
    val tgl_setoran: String? = null
)

data class KomponenSetoranRequest(
    val nama_komponen_setoran: String,
    val id_komponen_setoran: String
)
data class HapusSetoranRequest(
    val data_setoran: List<HapusKomponen>
)

data class HapusKomponen(
    val id: String,
    val id_komponen_setoran: String,
    val nama_komponen_setoran: String
)
