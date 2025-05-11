package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("trackId") val trackId: Long?,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?, // URL for a 100x100 pixel artwork
    @SerializedName("collectionName") val previewUrl: String?
)