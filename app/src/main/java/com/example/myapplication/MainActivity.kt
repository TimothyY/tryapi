package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.Toast
import android.content.Context
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class MainActivity : AppCompatActivity() {

    private val itunesApi = RetrofitClient.instance
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchItunes("Linkin+Park",this);
    }

    fun searchItunes(searchTerm: String, context: Context) {

        mainScope.launch { // Launch a coroutine in the main scope
            try {
                // Use withContext to switch to the IO dispatcher for network operations
                val response = withContext(Dispatchers.IO) {
                    itunesApi.search(searchTerm).execute() // Use execute() for synchronous call
                }

                if (response.isSuccessful) {
                    // Switch back to the main thread to update the UI or show results
                    val searchResponse = response.body()
                    if (searchResponse != null) {
                        val results = searchResponse.results
                        if (results != null && results.isNotEmpty()) {
                            // Handle the successful response here
                            for (result in results) {
                                Log.d("ITunesSearch", "Track: ${result.trackName}, Artist: ${result.artistName}")
                                // Update UI with results (e.g., in a RecyclerView)
                            }
                            // Example of showing the first track name in a Toast:
                            val firstTrackName = results.firstOrNull()?.trackName ?: "No tracks found"
                            Toast.makeText(context, "First Track: $firstTrackName", Toast.LENGTH_LONG).show()

                        } else {
                            Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show()
                            Log.d("ITunesSearch", "No results found")
                        }
                    } else {
                        Toast.makeText(context, "Empty response body", Toast.LENGTH_LONG).show()
                        Log.e("ITunesSearch", "Empty response body")
                    }
                } else {
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("ITunesSearch", errorMessage)
                }
            } catch (e: Exception) {
                // Handle network errors (important!)
                val errorMessage = when (e) {
                    is SocketTimeoutException -> "Timeout: Server took too long to respond."
                    is UnknownHostException -> "No Internet Connection"
                    is SSLException -> "Problem with the server's security certificate" //Handle SSL
                    else -> "An unexpected error occurred: ${e.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                Log.e("ITunesSearch", "Exception: $errorMessage", e)
            }
        }
    }
}