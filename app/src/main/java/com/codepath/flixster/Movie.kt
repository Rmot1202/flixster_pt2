package com.codepath.flixster

import com.google.gson.annotations.SerializedName

/**
 * Model for a single movie from the TMDB API.
 */
class Movie {

    @JvmField
    @SerializedName("title")
    var title: String? = null

    @JvmField
    @SerializedName("overview")
    var overview: String? = null

    // Partial paths from TMDB (e.g., "/ct6HkcvSGDC5yT6eQKdBn4HWcNC.jpg")
    @JvmField
    @SerializedName("poster_path")
    var posterPath: String? = null

    @JvmField
    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    // Convenience full URLs for Glide
    val posterImageUrl: String?
        get() = posterPath?.let { IMAGE_BASE_URL + it }

    val backdropImageUrl: String?
        get() = backdropPath?.let { IMAGE_BASE_URL + it }

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
    }
}
