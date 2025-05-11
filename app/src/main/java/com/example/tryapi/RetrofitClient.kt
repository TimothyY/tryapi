package com.example.tryapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val ITUNES_BASE_URL = "https://itunes.apple.com/"

    // this is only as an example for if an app need to use multiple different base_url (1)
    private const val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"

    // Use lazy initialization for thread safety and efficiency
    val itunesInstance: ItunesApi by lazy {
        createRetrofit(ITUNES_BASE_URL).create(ItunesApi::class.java)
    }

    // this is only as an example for if an app need to use multiple different base_url (2),
    // adjust the <api interface>.kt and response data class accordingly

    // New instance for Google Maps API (or any other API)
    val googleInstance: GoogleApi by lazy {  // Changed type to GoogleApi
        createRetrofit(GOOGLE_BASE_URL).create(GoogleApi::class.java) //changed to GoogleApi
    }

    // Function to create Retrofit instance
    private fun createRetrofit(baseUrl: String): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}