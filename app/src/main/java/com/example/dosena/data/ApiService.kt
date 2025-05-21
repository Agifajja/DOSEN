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
        @Field("client_secret") clientSecret: String = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSI",
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Response<LoginResponse>

    @GET("setoran-dev/v1/dosen/pa-saya")
    suspend fun getMahasiswaPA(
        @Header("Authorization") token: String
    ): Response<PaSayaResponse>
}
