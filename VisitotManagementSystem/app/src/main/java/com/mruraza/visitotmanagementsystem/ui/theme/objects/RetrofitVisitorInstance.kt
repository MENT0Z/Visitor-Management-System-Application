package com.mruraza.visitotmanagementsystem.ui.theme.objects


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mruraza.visitotmanagementsystem.ui.theme.interfaces.VisitorApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitVisitorClient {
    private const val BASE_URL = "http://10.0.2.2:8085" // Ensure the trailing slash

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val gson: Gson = GsonBuilder().setLenient().create()

    val instance: VisitorApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(VisitorApiService::class.java)
    }
}
