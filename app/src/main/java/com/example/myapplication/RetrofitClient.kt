package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://itunes.apple.com/"

    // Use lazy initialization for thread safety and efficiency
    val instance: ItunesApi by lazy {
        // Create an OkHttpClient and add logging for debugging
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Log request and response
            .connectTimeout(30, TimeUnit.SECONDS) // Increased timeout
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Build the Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
            .client(httpClient) // Set the OkHttpClient
            .build()

        // Create and return the API interface implementation.
        retrofit.create(ItunesApi::class.java)
    }
}