package com.example.smartmailbox.api.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer 9ea96945-3a37-4638-a5d4-22e89fbc998f")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: MailBoxAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-d4me-stage.direct4.me/sandbox/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MailBoxAPI::class.java)
    }
}