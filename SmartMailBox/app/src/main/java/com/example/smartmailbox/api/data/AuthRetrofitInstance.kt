package com.example.smartmailbox.api.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitInstance {
    // needs changing when it goes on different network
    private const val BASE_URL = "http://192.168.2.51:3001/"

    // retrofit sets the correct header automatically
    private val client = OkHttpClient.Builder()
        .cookieJar(SessionCookieJar())
        .build()

    val api: AuthAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }
}