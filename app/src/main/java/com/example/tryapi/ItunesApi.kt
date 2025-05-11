package com.example.tryapi
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("search")
    fun search(
        @Query("term") term: String
    ): Call<SearchResponse>
    // Example:
    // https://itunes.apple.com/search?term=jack+johnson
}

data class SearchResponse(
    @SerializedName("results") val results: List<SearchResult>?, // Use ? for null safety
    @SerializedName("resultCount") val resultCount: Int?
)

data class SearchResult(
    @SerializedName("trackId") val trackId: Long?,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?, // URL for a 100x100 pixel artwork
    @SerializedName("collectionName") val collectionName: String?
)