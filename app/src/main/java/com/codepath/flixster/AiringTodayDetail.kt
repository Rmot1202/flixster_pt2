package com.codepath.flixster

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Model for a single TV show (TMDB /tv/airing_today).
 */
class AiringTodayDetail : Serializable {
    @SerializedName("id") var id: Int? = null
    @SerializedName("name")           var title: String? = null
    @SerializedName("vote_average")   var rating: Double? = null
    @SerializedName("vote_count")     var voteCount: Int? = null
    @SerializedName("poster_path")    var posterPath: String? = null
    @SerializedName("overview")       var overview: String? = null
    @SerializedName("first_air_date") var releaseDate: String? = null
    @SerializedName("backdrop_path")  var backdropPath: String? = null

    val posterImageUrl: String?
        get() = posterPath?.let { IMAGE + it }

    val backdropImageUrl: String?
        get() = backdropPath?.let { BACKDROP + it }

    companion object {
        private const val IMAGE = "https://image.tmdb.org/t/p/w500"
        private const val BACKDROP = "https://image.tmdb.org/t/p/w780"
        // Optional: for Java serialization versioning
        @JvmStatic private val serialVersionUID = 1L
    }
}
