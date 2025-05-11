package com.example.tryapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.Toast
import android.widget.TextView
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class MainActivity : AppCompatActivity() {

    private val itunesApi = RetrofitClient.itunesInstance
    private val googleApi = RetrofitClient.googleInstance //sample only for multiple different baseurl api

    lateinit var tvClickToSearch: TextView // Declare TextView
    lateinit var tvResult: TextView // Declare TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvClickToSearch = findViewById(R.id.tvClickToSearch)
        tvClickToSearch.setOnClickListener {
            // Call searchItunes when the TextView is clicked
            searchItunes("Linkin+Park");
        }
        tvResult = findViewById(R.id.tvResult)
    }

    fun searchItunes(searchTerm: String) {
        val call = itunesApi.search(searchTerm)
        //use the enqueue() from retrofit2.Call to call api asynchrnously
        call.enqueue(object: Callback<SearchResponse>{
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    // Switch back to the main thread to update the UI or show results
                    val searchResponse = response.body()
                    if (searchResponse != null) {
                        val results = searchResponse.results
                        if (results != null && results.isNotEmpty()) {
                            // Handle the successful response here
                            var allResults:String=""
                            for (result in results) {
                                allResults+="Track: ${result.trackName}, Artist: ${result.artistName}, Album: ${result.collectionName}\n\n"
                                // Update UI with results (e.g., in a RecyclerView)
                            }
//                            Toast.makeText(context, allResults, Toast.LENGTH_LONG).show()
                            tvResult.setText(allResults)
                        } else {
                            Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_LONG).show()
                            Log.d("ITunesSearch", "No results found")
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Empty response body", Toast.LENGTH_LONG).show()
                        Log.e("ITunesSearch", "Empty response body")
                    }
                } else {
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("ITunesSearch", errorMessage)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is SocketTimeoutException -> "Timeout: Server took too long to respond."
                    is UnknownHostException -> "No Internet Connection"
                    is SSLException -> "Problem with the server's security certificate"
                    else -> "An unexpected error occurred: ${t.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                Log.e("ITunesSearch", "Exception: $errorMessage", t)
            }
        })
    }

    fun searchGoogleApi() {
        val call = googleApi.getGeocode("1600 Amphitheatre Parkway, Mountain View, CA", "YOUR_API_KEY") // Replace with your API key
        call.enqueue(object : Callback<GeocodeResponse> {
            override fun onResponse(call: Call<GeocodeResponse>, response: Response<GeocodeResponse>) {
                if (response.isSuccessful) {
                    val geocodeResponse = response.body()
                    if (geocodeResponse != null) {
                        val location = geocodeResponse.results?.firstOrNull()?.geometry?.location
                        if (location != null) {
                            Log.d("GoogleMaps", "Latitude: ${location.lat}, Longitude: ${location.lng}")
                            Toast.makeText(this@MainActivity, "Location found!", Toast.LENGTH_LONG).show()
                        } else {
                            Log.e("GoogleMaps", "Location not found")
                            Toast.makeText(this@MainActivity, "Location not found", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.e("GoogleMaps", "Empty response body")
                        Toast.makeText(this@MainActivity, "Empty response", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("GoogleMaps", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Error fetching location", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GeocodeResponse>, t: Throwable) {
                val errorMessage = when (t) {
                    is SocketTimeoutException -> "Timeout: Server took too long to respond."
                    is UnknownHostException -> "No Internet Connection"
                    is SSLException -> "Problem with the server's security certificate"
                    else -> "An unexpected error occurred: ${t.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                Log.e("GoogleMaps", "Exception: $errorMessage", t)
            }
        })
    }
}