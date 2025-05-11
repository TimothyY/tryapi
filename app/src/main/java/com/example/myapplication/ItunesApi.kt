package com.example.myapplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search")
    fun search(
        @Query("term") term: String
    ): Call<SearchResponse>
    // Example:
    // https://itunes.apple.com/search?term=jack+johnson&entity=music
}