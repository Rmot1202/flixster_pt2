package com.codepath.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

// --------------------------------//
// COURSE API KEY (OK for this hw) //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class FlixsterFragment : Fragment(), OnListFragmentInteractionListener {

    private lateinit var progressBar: ContentLoadingProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: flixsterRecyclerViewAdapter
    private val movies = mutableListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_flixster_list, container, false)

        progressBar = view.findViewById(R.id.progress)
        recyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // Attach adapter immediately (silences "No adapter attached; skipping layout")
        adapter = flixsterRecyclerViewAdapter(movies, this@FlixsterFragment)
        recyclerView.adapter = adapter

        updateAdapter()
        return view
    }

    /**
     * Fetch now_playing from TMDB and update RecyclerView.
     */
    private fun updateAdapter() {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams().apply { this["api_key"] = API_KEY }
        val url = "https://api.themoviedb.org/3/movie/now_playing"

        client[url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    // TMDB payload: {..., "results": [ {movie}, ... ] }
                    val results = json.jsonObject.getJSONArray("results")

                    val listType = object : TypeToken<List<Movie>>() {}.type
                    val newMovies: List<Movie> =
                        Gson().fromJson(results.toString(), listType)

                    // Update UI
                    recyclerView.post {
                        movies.clear()
                        movies.addAll(newMovies)
                        adapter.notifyDataSetChanged()
                        progressBar.hide()
                    }

                    Log.d("FlixsterFragment", "Loaded ${newMovies.size} movies")
                } catch (e: Exception) {
                    Log.e("FlixsterFragment", "Parse error", e)
                    recyclerView.post {
                        progressBar.hide()
                        Toast.makeText(requireContext(), "Failed to parse movies", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("FlixsterFragment", "HTTP $statusCode: $response", throwable)
                recyclerView.post {
                    progressBar.hide()
                    Toast.makeText(requireContext(), "Network error ($statusCode)", Toast.LENGTH_SHORT).show()
                }
            }
        }]
    }

    override fun onItemClick(item: Movie) {
        TODO("Not yet implemented")
    }
}
