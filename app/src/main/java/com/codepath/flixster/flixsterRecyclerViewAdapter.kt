package com.codepath.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FlixsterRecyclerViewAdapter(
    private var movies: List<Movie>,
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<FlixsterRecyclerViewAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_flixster, parent, false) // <-- compact item (poster+title+rating)
        return MovieViewHolder(v)
    }

    inner class MovieViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val ivPoster: ImageView = v.findViewById(R.id.ivPoster)
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvRating: TextView = v.findViewById(R.id.tvRating)
        var item: Movie? = null

        init {
            v.setOnClickListener { item?.let { listener?.onItemClick(it) } }
        }
    }

    override fun onBindViewHolder(h: MovieViewHolder, position: Int) {
        val movie = movies[position]
        h.item = movie

        h.tvTitle.text = movie.title ?: "Untitled"
        h.tvRating.text = "⭐ " + (movie.rating?.let { String.format("%.1f", it) } ?: "N/A")

        val url = movie.posterImageUrl ?: movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
        Glide.with(h.v)
            .load(url)
            .centerCrop()
            .into(h.ivPoster)
    }

    override fun getItemCount() = movies.size

    fun submitList(newList: List<Movie>) {
        movies = newList
        notifyDataSetChanged()
    }
}
