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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AiringTodayAdapter
    private val items = mutableListOf<AiringTodayDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_flixster_list, container, false)

        progressBar = view.findViewById(R.id.progress)
        recyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.setHasFixedSize(true)
        PagerSnapHelper().attachToRecyclerView(recyclerView) // optional

        // Adapter takes a click lambda
        adapter = AiringTodayAdapter(items) { item -> openDetail(item) }
        recyclerView.adapter = adapter

        loadAiringToday()
        return view
    }

    /** Fetch TV /airing_today and update RecyclerView. */
    private fun loadAiringToday() {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams().apply {
            this["api_key"] = API_KEY
            // optional:
            // this["language"] = "en-US"
            // this["page"] = "1"
        }
        val url = "https://api.themoviedb.org/3/tv/airing_today"

        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    val listType = object : TypeToken<List<AiringTodayDetail>>() {}.type
                    val newItems: List<AiringTodayDetail> =
                        Gson().fromJson(results.toString(), listType)

                    recyclerView.post {
                        items.clear()
                        items.addAll(newItems)
                        adapter.notifyDataSetChanged()
                        progressBar.hide()
                    }

                    Log.d("FlixsterFragment", "Loaded ${newItems.size} shows")
                } catch (e: Exception) {
                    Log.e("FlixsterFragment", "Parse error", e)
                    recyclerView.post {
                        progressBar.hide()
                        Toast.makeText(requireContext(), "Failed to parse shows", Toast.LENGTH_SHORT).show()
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
        })
    }

    private fun openDetail(item: AiringTodayDetail) {
        val i = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(EXTRA_AIRING_TODAY, item) // AiringTodayDetail must be Serializable or Parcelable
        }
        startActivity(i)
    }
}
