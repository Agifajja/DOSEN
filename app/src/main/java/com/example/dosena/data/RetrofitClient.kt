package com.example.dosena.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val authApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://id.tif.uin-suska.ac.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    val mainApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.tif.uin-suska.ac.id/setoran-dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
