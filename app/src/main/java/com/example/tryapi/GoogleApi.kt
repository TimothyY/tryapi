package com.example.tryapi
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {  // Added GoogleApi interface
    @GET("geocode/json") // Example endpoint
    fun getGeocode(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Call<GeocodeResponse> // Created a new Response class
}

//Added new data class
data class GeocodeResponse(
    @SerializedName("results") val results: List<GeocodeResult>?,
    @SerializedName("status") val status: String?
)

data class GeocodeResult (
    @SerializedName("geometry") val geometry: Geometry?
)

data class Geometry(
    @SerializedName("location") val location: Location?
)

data class Location (
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?
)