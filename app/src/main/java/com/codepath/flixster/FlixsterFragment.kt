package com.codepath.flixster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
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

class FlixsterFragment : Fragment() {

    private lateinit var progressBar: ContentLoadingProgressBar

    private lateinit var rvAiring: RecyclerView
    private lateinit var rvTop: RecyclerView

    private lateinit var airingAdapter: AiringTodayAdapter
    private lateinit var topAdapter: AiringTodayAdapter

    private val airingItems = mutableListOf<AiringTodayDetail>()
    private val topItems = mutableListOf<AiringTodayDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_flixster_list, container, false)

        progressBar = view.findViewById(R.id.progress)
        rvAiring = view.findViewById(R.id.listAiring)
        rvTop = view.findViewById(R.id.listTopRated)

        // Horizontal carousels
        rvAiring.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        rvTop.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        // Optional: snap page-by-page on the first list
        PagerSnapHelper().attachToRecyclerView(rvAiring)

        // Avoid nested scroll jitters
        rvAiring.isNestedScrollingEnabled = false
        rvTop.isNestedScrollingEnabled = false
        rvAiring.setHasFixedSize(true)
        rvTop.setHasFixedSize(true)

        airingAdapter = AiringTodayAdapter(airingItems) { item -> openDetail(item) }
        topAdapter = AiringTodayAdapter(topItems) { item -> openDetail(item) }

        rvAiring.adapter = airingAdapter
        rvTop.adapter = topAdapter

        // Load both sections
        loadAiringToday()
        loadTopRated()

        return view
    }

    /** Fetch TV /airing_today and update top carousel. */
    private fun loadAiringToday() {
        progressBar.show()
        val client = AsyncHttpClient()
        val params = RequestParams().apply { this["api_key"] = API_KEY }
        val url = "https://api.themoviedb.org/3/tv/airing_today"

        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    val listType = object : TypeToken<List<AiringTodayDetail>>() {}.type
                    val newItems: List<AiringTodayDetail> = Gson().fromJson(results.toString(), listType)

                    rvAiring.post {
                        airingItems.clear()
                        airingItems.addAll(newItems)
                        airingAdapter.notifyDataSetChanged()
                        progressBar.hide()
                    }
                    Log.d("FlixsterFragment", "AiringToday: ${newItems.size}")
                } catch (e: Exception) {
                    Log.e("FlixsterFragment", "Parse error (airing_today)", e)
                    rvAiring.post {
                        progressBar.hide()
                        Toast.makeText(requireContext(), "Failed to parse Airing Today", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(code: Int, headers: Headers?, resp: String?, t: Throwable?) {
                Log.e("FlixsterFragment", "HTTP $code (airing_today): $resp", t)
                rvAiring.post {
                    progressBar.hide()
                    Toast.makeText(requireContext(), "Network error ($code)", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /** Fetch TV /top_rated and update bottom carousel. */
    private fun loadTopRated() {
        progressBar.show()
        val client = AsyncHttpClient()
        val params = RequestParams().apply { this["api_key"] = API_KEY }
        val url = "https://api.themoviedb.org/3/tv/top_rated"

        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    val listType = object : TypeToken<List<AiringTodayDetail>>() {}.type
                    val newItems: List<AiringTodayDetail> = Gson().fromJson(results.toString(), listType)

                    rvTop.post {
                        topItems.clear()
                        topItems.addAll(newItems)
                        topAdapter.notifyDataSetChanged()
                        progressBar.hide()
                    }
                    Log.d("FlixsterFragment", "TopRated: ${newItems.size}")
                } catch (e: Exception) {
                    Log.e("FlixsterFragment", "Parse error (top_rated)", e)
                    rvTop.post {
                        progressBar.hide()
                        Toast.makeText(requireContext(), "Failed to parse Top Rated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(code: Int, headers: Headers?, resp: String?, t: Throwable?) {
                Log.e("FlixsterFragment", "HTTP $code (top_rated): $resp", t)
                rvTop.post {
                    progressBar.hide()
                    Toast.makeText(requireContext(), "Network error ($code)", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun openDetail(item: AiringTodayDetail) {
        val i = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_AIRING_TODAY, item) // AiringTodayDetail is Serializable
        }
        startActivity(i)
    }
}
