package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("results") val results: List<SearchResult>?, // Use ? for null safety
    @SerializedName("resultCount") val resultCount: Int?
)