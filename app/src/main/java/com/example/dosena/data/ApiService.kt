package com.example.dosena.data

import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun login(
        @Field("client_id") clientId: String = "setoran-mobile-dev",
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl", // konsisten
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun refreshToken(
        @Field("client_id") clientId: String = "setoran-mobile-dev",
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl", // perbaiki huruf terakhir (bukan I besar)
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Response<LoginResponse>

    @GET("dosen/pa-saya")
    suspend fun getMahasiswaPA(
        @Header("Authorization") token: String
    ): Response<PaSayaResponse>

    @GET("mahasiswa/setoran/{nim}")
    suspend fun getSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<SetoranMahasiswaResponse>

    @POST("mahasiswa/setoran/{nim}")
    suspend fun simpanSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body requestBody: SimpanSetoranRequest
    ): Response<GeneralResponse>

    @HTTP(method = "DELETE", path = "mahasiswa/setoran/{nim}", hasBody = true)
    suspend fun deleteSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body requestBody: DeleteSetoranRequest
    ): Response<GeneralResponse>

    @POST("setoran-mahasiswa/{nim}/tambah")
    suspend fun tambahSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body request: TambahEditSetoranRequest
    ): Response<GeneralResponse>

    @PUT("setoran-mahasiswa/{id}/edit")
    suspend fun editSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: TambahEditSetoranRequest
    ): Response<GeneralResponse>


}
