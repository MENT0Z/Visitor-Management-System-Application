package com.mruraza.visitotmanagementsystem.ui.theme.objects

import com.mruraza.visitotmanagementsystem.ui.theme.interfaces.VisitorApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// to avd
//.baseUrl("http://10.0.2.2:5000")
// to usb
//.baseUrl("http://10.3.83.231:5000")
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8085" // Replace with your local server IP if running on a real device

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: VisitorApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(VisitorApi::class.java)
    }
}