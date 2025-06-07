package com.example.dosena.data

import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun login(
        @Field("client_id") clientId: String = "setoran-mobile-dev",
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl",
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun refreshToken(
        @Field("client_id") clientId: String = "setoran-mobile-dev",
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl",
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



    @HTTP(method = "DELETE", path = "mahasiswa/setoran/{nim}", hasBody = true)
    suspend fun deleteSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body requestBody: HapusSetoranRequest
    ): Response<GeneralResponse>



    // ✅ Ubah endpoint edit jadi sesuai instruksi baru
    @PUT("mahasiswa/setoran/{nim}")
    suspend fun editSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String,
        @Body request: SimpanSetoranRequest
    ): Response<GeneralResponse>
}