package com.codepath.flixster

import com.google.gson.annotations.SerializedName

/**
 * Model for a single movie from the TMDB API.
 */
class Movie: java.io.Serializable{

    @JvmField
    @SerializedName("name")
    var title: String? = null

    @JvmField
    @SerializedName("vote_average")
    var rating: Double? = null

    // Partial paths from TMDB (e.g., "/ct6HkcvSGDC5yT6eQKdBn4HWcNC.jpg")
    @JvmField
    @SerializedName("poster_path")
    var posterPath: String? = null


    // Convenience full URLs for Glide
    val posterImageUrl: String?
        get() = posterPath?.let { IMAGE_BASE_URL + it }

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}
